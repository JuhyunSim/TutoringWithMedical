import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import axios from 'axios';
import './Signup.css';

// Define subjects according to the enum in the backend
const subjectsList = [
    { value: "ELEMENTARY_MATH", label: "초등수학" },
    { value: "MIDDLE_MATH", label: "중등수학" },
    { value: "HIGH_MATH", label: "고등수학" },
    { value: "ELEMENTARY_ENGLISH", label: "초등영어" },
    { value: "MIDDLE_ENGLISH", label: "중등영어" },
    { value: "HIGH_ENGLISH", label: "고등영어" }
];

const universityList = [
    { value: "SEOUL_UNIVERSITY", label: "서울대학교" },
    { value: "KOREA_UNIVERSITY", label: "고려대학교" },
    { value: "YONSEI_UNIVERSITY", label: "연세대학교" },
    { value: "SUNKYUNKWAN_UNIVERSITY", label: "성균관대학교" }
    // Add more universities as needed
];

// 추가된 학년 옵션
const tuteeGradeList = [
    { value: "ELEMENTARY_1", label: "초등학교 1학년" },
    { value: "ELEMENTARY_2", label: "초등학교 2학년" },
    { value: "ELEMENTARY_3", label: "초등학교 3학년" },
    { value: "ELEMENTARY_4", label: "초등학교 4학년" },
    { value: "ELEMENTARY_5", label: "초등학교 5학년" },
    { value: "ELEMENTARY_6", label: "초등학교 6학년" },
    { value: "MIDDLE_1", label: "중학교 1학년" },
    { value: "MIDDLE_2", label: "중학교 2학년" },
    { value: "MIDDLE_3", label: "중학교 3학년" },
    { value: "HIGH_1", label: "고등학교 1학년" },
    { value: "HIGH_2", label: "고등학교 2학년" },
    { value: "HIGH_3", label: "고등학교 3학년" }
];

const locationList = [
    { value: "SEOUL", label: "서울" },
    { value: "BUSAN", label: "부산" },
    { value: "INCHEON", label: "인천" },
    // Add more locations as needed
];

const enrollmentStatusList = [
    { value: "ENROLLED", label: "재학" },
    { value: "LEAVE_OF_ABSENCE", label: "휴학" },
    { value: "GRADUATED", label: "졸업" },
    { value: "DROPPED_OUT", label: "중퇴" }
];

const Signup = () => {
    const [step, setStep] = useState(1); // Tracks the current step
    const [nickname, setNickname] = useState('');
    const [gender, setGender] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const [password, setPassword] = useState('');
    const [passwordConfirm, setPasswordConfirm] = useState('');
    const [role, setRole] = useState('');
    const [tutorProfile, setTutorProfile] = useState({
        proofFile: null,
        imageUrl: null,
        proofFilePreview: null, 
        profileImagePreview: null,
        subjects: [],
        location: '',
        description: '',
        university: '',
        status: '',
    });
    const [tuteeProfile, setTuteeProfile] = useState({
        tuteeName: '',
        gender: '',
        location: '',
        subjects: [],
        description: '',
        tuteeGrade: '',
    });
    const [tuteeProfiles, setTuteeProfiles] = useState([
        { 
            tuteeName: '',
            gender: '', 
            location: '', 
            subjects: [], 
            tuteeGrade: '', 
            description: '' 
        },
    ]);
    const [currentProfileIndex, setCurrentProfileIndex] = useState(0);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // const sendVerificationCode = async () => {
    //     setError('');
    //     try {
    //         await axios.post(`${process.env.REACT_APP_BACKEND_URL}/send-one?receivePhone=${phoneNumber}`);
    //         alert('인증번호가 전송되었습니다.');
    //         setStep(2); // Move to verification code entry step
    //     } catch (err) {
    //         setError('인증번호 전송에 실패했습니다. 다시 시도해 주세요.');
    //         console.error(err);
    //     }
    // };

    //verify the code
    const validateVerificationCode = async () => {
        setError('');
        try {
            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/auth/login/verify?receivePhone=${phoneNumber}&verifyNumber=${verificationCode}`);
            nextStep(); // Move to the next step only if validation is successful
        } catch (err) {
            setError('잘못된 인증번호입니다. 다시 확인해 주세요.');
            console.error(err);
        }
    };

    const handlePreviousProfile = () => {
        if (currentProfileIndex > 0) {
            setCurrentProfileIndex((prevIndex) => prevIndex - 1);
        }
    };

    const handleSignup = async (e) => {
        e.preventDefault();
        const requestBody = {
            nickname,
            gender,
            phoneNumber,
            password,
            passwordConfirm,
            role,
            tutorProfile: role === 'TUTOR' ? tutorProfile : null,
            tuteeProfile: role === 'TUTEE' ? tuteeProfile : null,
            tuteeProfiles: role === 'PARENT' ? tuteeProfiles : [],
        };

        console.log(requestBody); // requestBody에서 location 값 확인

        try {
            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/signup`, requestBody);
            navigate('/login');
            alert('회원가입이 완료되었습니다!');
        } catch (error) {
            console.error(error);
            setError('회원가입에 실패했습니다. 입력 정보를 확인해주세요.');
        }
    };

    const nextStep = () => setStep((prev) => prev + 1);
    const prevStep = () => setStep((prev) => prev - 1);

    const handleFileChange = async (e, field) => {
        const file = e.target.files[0];
        if (!file) return;

        // Update preview URL
        const reader = new FileReader();
        reader.onloadend = () => {
            setTutorProfile((prev) => ({
                ...prev,
                [`${field}Preview`]: reader.result, // Update preview field
            }));
        };
        reader.readAsDataURL(file);

        // Upload the file to the server
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post(
                `${process.env.REACT_APP_BACKEND_URL}/api/images/upload`,
                formData,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            );
            setTutorProfile((prev) => ({
                ...prev,
                [field]: response.data, // Save the uploaded file URL
            }));
        } catch (err) {
            console.error('Failed to upload file:', err);
            setError('이미지 업로드에 실패했습니다. 다시 시도해 주세요.');
        }
    };

    const handleAddTuteeProfile = () => {
        const currentProfile = tuteeProfiles[currentProfileIndex];

        if (
            !currentProfile.tuteeName ||
            !currentProfile.gender ||
            !currentProfile.location ||
            currentProfile.subjects.length === 0 ||
            !currentProfile.tuteeGrade
        ) {
            alert('모든 필수 정보를 입력해주세요: 이름, 성별, 지역, 과목, 학년.');
            return;
        }
        
        setTuteeProfiles(
            [...tuteeProfiles, 
            { 
                name: '',
                gender: '', 
                location: '', 
                subjects: [], 
                description: '', 
                tuteeGrade: '' 
            }]);
        setCurrentProfileIndex((prevIndex) => prevIndex + 1);
    };

    const handleProfileChange = (field, value) => {
        // 방어 코드 추가
        if (currentProfileIndex < 0 || currentProfileIndex >= tuteeProfiles.length) {
            console.error("Invalid profile index");
            return;
        }

        const updatedProfiles = [...tuteeProfiles];
        updatedProfiles[currentProfileIndex][field] = value;
        setTuteeProfiles(updatedProfiles);
    };

    const handleSubjectsChange = (selectedSubjects, role) => {
        if (role === 'TUTOR') {
            setTutorProfile({ ...tutorProfile, subjects: selectedSubjects });
        } else if (role === 'STUDENT') {
            setTuteeProfile({ ...tuteeProfile, subjects: selectedSubjects });
        }
    };

    const toggleSubject = (subject, profileType) => {
        const updateSubjects = (selectedSubjects) => 
            selectedSubjects.includes(subject)
                ? selectedSubjects.filter((s) => s !== subject)
                : [...selectedSubjects, subject];

        if (profileType === 'TUTOR') {
            setTutorProfile((prev) => ({ ...prev, subjects: updateSubjects(prev.subjects) }));
        } else if (profileType === 'STUDENT') {
            setTuteeProfile((prev) => ({ ...prev, subjects: updateSubjects(prev.subjects) }));
        } else if (profileType === 'PARENT') {
            const updatedProfiles = [...tuteeProfiles];
            updatedProfiles[currentProfileIndex].subjects = updateSubjects(
                tuteeProfiles[currentProfileIndex]?.subjects || []
            );
            setTuteeProfiles(updatedProfiles);
        }
    };


    return (
        <div className="signup-container">
            <h2>회원가입</h2>

            {/* Step 1: Basic Information */}
            {step === 1 && (
                <>
                    <div className="input-group">
                        <label>Nickname</label>
                        <input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} required />
                    </div>

                    <div className="input-group">
                        <label>Gender</label>
                        <select value={gender} onChange={(e) => setGender(e.target.value)} required>
                            <option value="">Select Gender</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </select>
                    </div>

                    <div className="input-group">
                        <label>Phone Number</label>
                        <input type="text" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} required />
                        {/* <button type="button" onClick={sendVerificationCode} className="verify-button">인증</button> */}
                    </div>
                    <button onClick={nextStep}>다음</button>
                </>
            )}

            {/* Step 2: Verification Code */}
            {/* {step === 2 && (
                <>
                    <div className="input-group">
                        <label>Verification Code</label>
                        <input type="text" value={verificationCode} onChange={(e) => setVerificationCode(e.target.value)} required />
                    </div>
                    <button onClick={prevStep}>이전</button>
                    <button onClick={validateVerificationCode}>인증번호 확인</button>
                </>
            )} */}

            {/* Step 2: Password and Role */}
            {step === 2 && (
                <>
                    <div className="input-group">
                        <label>Password</label>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>

                    <div className="input-group">
                        <label>Confirm Password</label>
                        <input type="password" value={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)} required />
                    </div>

                    <div className="input-group">
                        <label>Role</label>
                        <select value={role} onChange={(e) => setRole(e.target.value)} required>
                            <option value="">Select Role</option>
                            <option value="TUTOR">Tutor</option>
                            <option value="TUTEE">Student</option>
                            <option value="PARENT">Parent</option>
                        </select>
                    </div>
                    <button onClick={prevStep}>이전</button>
                    <button onClick={nextStep}>다음</button>
                </>
            )}

            {/* Step 3: Profile Details Based on Role */}
            {step === 3 && (
                <>
                    <>
                    {role === 'TUTOR' && (
                        <>
                            <div className="input-group">
                                <label>Proof File</label>
                                <input type="file" onChange={(e) => handleFileChange(e, 'proofFile')} />
                            </div>
                            <div className="input-group">
                                <label>Profile Image</label>
                                <div className="profile-image-container">
                                    {tutorProfile.profileImagePreview ? (
                                        <img
                                            src={tutorProfile.profileImagePreview}
                                            alt="Profile Preview"
                                            className="preview-image"
                                        />
                                    ) : (
                                        <div className="placeholder-image">이미지 없음</div>
                                    )}
                                </div>
                                <input
                                    type="file"
                                    onChange={(e) => handleFileChange(e, 'imageUrl')}
                                    accept="image/*"
                                />
                            </div>
                            <div className="input-group">
                                <label>Subjects</label>
                                <div className="chip-container">
                                    {subjectsList.map((subject) => (
                                        <div
                                            key={subject.value}
                                            className={`chip ${tutorProfile.subjects.includes(subject.value) ? 'selected' : ''}`}
                                            onClick={() => toggleSubject(subject.value, 'TUTOR')}
                                        >
                                            {subject.label}
                                        </div>
                                    ))}
                                </div>
                            </div>
                            <div className="input-group">
                                <label>Location</label>
                                <select value={tutorProfile.location} onChange={(e) => setTutorProfile({ ...tutorProfile, location: e.target.value })}>
                                    <option value="">Select Location</option>
                                    {locationList.map((location) => (
                                        <option key={location.value} value={location.value}>{location.label}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="input-group">
                                <label>University</label>
                                <select value={tutorProfile.university} onChange={(e) => setTutorProfile({ ...tutorProfile, university: e.target.value })}>
                                    <option value="">Select University</option>
                                    {universityList.map((uni) => (
                                        <option key={uni.value} value={uni.value}>{uni.label}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="input-group">
                                <label>Enrollment Status</label>
                                <select value={tutorProfile.status} onChange={(e) => setTutorProfile({ ...tutorProfile, status: e.target.value })}>
                                    <option value="">Select Enrollment Status</option>
                                    {enrollmentStatusList.map((status) => (
                                        <option key={status.value} value={status.value}>{status.label}</option>
                                    ))}
                                </select>
                            </div>
                        </>
                    )}

                    {role === 'TUTEE' && (
                        <>
                            <div className="input-group">
                                <label>이름</label>
                                <input
                                    type="text"
                                    value={tuteeProfile.tuteeName}
                                    onChange={(e) => setTuteeProfile({ ...tuteeProfile, tuteeName: e.target.value })}
                                />
                            </div>

                            <div className="input-group">
                                <label>성별</label>
                                <select
                                    value={tuteeProfile.gender}
                                    onChange={(e) => setTuteeProfile({...tuteeProfile, gender: e.target.value})}
                                >
                                    <option value="">Select Gender</option>
                                    <option value="MALE">Male</option>
                                    <option value="FEMALE">Female</option>
                                </select>
                            </div>

                            <div className="input-group">
                                <label>Location</label>
                                <select value={tuteeProfile.location} onChange={(e) => setTuteeProfile({ ...tuteeProfile, location: e.target.value })}>
                                    <option value="">Select Location</option>
                                    {locationList.map((location) => (
                                        <option key={location.value} value={location.value}>{location.label}</option>
                                    ))}
                                </select>
                            </div>
                            
                            <div className="input-group">
                                <label>Subjects</label>
                                <div className="chip-container">
                                    {subjectsList.map((subject) => (
                                        <div
                                            key={subject.value}
                                            className={`chip ${tuteeProfile.subjects.includes(subject.value) ? 'selected' : ''}`}
                                            onClick={() => toggleSubject(subject.value, 'STUDENT')}
                                        >
                                            {subject.label}
                                        </div>
                                    ))}
                                </div>
                            </div>

                            <div className="input-group">
                                <label>Grade</label>
                                <select
                                    value={tuteeProfile.tuteeGrade}
                                    onChange={(e) => setTuteeProfile({ ...tuteeProfile, tuteeGrade: e.target.value })}
                                    required
                                >
                                    <option value="">Select Grade</option>
                                    {tuteeGradeList.map((grade) => (
                                        <option key={grade.value} value={grade.value}>{grade.label}</option>
                                    ))}
                                </select>
                            </div>
                        </>
                    )}

                    {/* Buttons for TUTOR and TUTEE */}
                    <div className="button-group">
                        <button onClick={prevStep}>이전</button>
                        <button onClick={handleSignup}>회원가입</button>
                    </div>
                    </>
                    {role === 'PARENT' && (
                        <>
                            <h3>학생 {currentProfileIndex + 1} 프로필</h3>
                            <div className="input-group">
                                <label>이름</label>
                                <input
                                    type="text"
                                    value={tuteeProfiles[currentProfileIndex]?.tuteeName || ''}
                                    onChange={(e) => handleProfileChange('tuteeName', e.target.value)} 
                                />
                            </div>

                            <div className="input-group">
                                <label>성별</label>
                                <select
                                    value={tuteeProfiles[currentProfileIndex]?.gender || ''}
                                    onChange={(e) => handleProfileChange('gender', e.target.value)}
                                >
                                    <option value="">Select Gender</option>
                                    <option value="MALE">Male</option>
                                    <option value="FEMALE">Female</option>
                                </select>
                            </div>

                            <div className="input-group">
                                <label>Location</label>
                                <select
                                    value={tuteeProfiles[currentProfileIndex].location}
                                    onChange={(e) => handleProfileChange('location', e.target.value)}
                                >
                                    <option value="">Select Location</option>
                                    {locationList.map((loc) => (
                                        <option key={loc.value} value={loc.value}>
                                            {loc.label}
                                        </option>
                                    ))}
                                </select>
                            </div>    

                            <div className="input-group">
                                <label>Subjects</label>
                                <div className="chip-container">
                                    {subjectsList.map((subject) => (
                                        <div
                                            key={subject.value}
                                            className={`chip ${
                                                tuteeProfiles[currentProfileIndex]?.subjects.includes(subject.value)
                                                    ? 'selected'
                                                    : ''
                                            }`}
                                            onClick={() => toggleSubject(subject.value, 'PARENT')}
                                        >
                                            {subject.label}
                                        </div>
                                    ))}
                                </div>
                            </div>

                            <div className="input-group">
                                <label>Grade</label>
                                <select
                                    value={tuteeProfiles[currentProfileIndex].tuteeGrade}
                                    onChange={(e) => handleProfileChange('tuteeGrade', e.target.value)}
                                >
                                    <option value="">Select Grade</option>
                                    {tuteeGradeList.map((grade) => (
                                        <option key={grade.value} value={grade.value}>
                                            {grade.label}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="input-group">
                                <label>소개</label>
                                <textarea
                                    value={tuteeProfiles[currentProfileIndex].description}
                                    onChange={(e) => handleProfileChange('description', e.target.value)}
                                />
                            </div>
                            <div className="button-group">
                                {currentProfileIndex > 0 ? (
                                    // 첫 번째 학생 프로필이 아닐 때 이전 프로필로 이동
                                    <button onClick={handlePreviousProfile}>이전</button>
                                ) : (
                                    // 첫 번째 학생 프로필일 때 이전 스텝으로 이동
                                    <button onClick={prevStep}>이전</button>
                                )}
                                <button onClick={handleAddTuteeProfile}>학생 프로필 추가하기</button>
                                <button onClick={handleSignup}>회원가입</button>
                            </div>                                          
                        </>
                    )}
                </>
                )}
                {error && <p className="error">{error}</p>}
        </div>
    );
};

export default Signup;
