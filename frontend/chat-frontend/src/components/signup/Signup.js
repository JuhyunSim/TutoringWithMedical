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

const locationList = [
    { value: "SEOUL", label: "서울" },
    { value: "BUSAN", label: "부산" },
    { value: "INCHEON", label: "인천" },
    // Add more locations as needed
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
        profileImage: null,
        subjects: [],
        location: '',
        description: '',
        university: '',
        status: '',
    });
    const [tuteeProfile, setTuteeProfile] = useState({
        location: '',
        subjects: [],
        description: '',
        tuteeGrade: '',
    });
    const [tuteeProfiles, setTuteeProfiles] = useState([]);
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

    const handleFileChange = (e, field) => {
        setTutorProfile({ ...tutorProfile, [field]: e.target.files[0] });
    };

    const handleAddTuteeProfile = () => {
        setTuteeProfiles([...tuteeProfiles, { location: '', subjects: [], description: '', tuteeGrade: '' }]);
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
                    {role === 'TUTOR' && (
                        <>
                            <div className="input-group">
                                <label>Proof File</label>
                                <input type="file" onChange={(e) => handleFileChange(e, 'proofFile')} />
                            </div>
                            <div className="input-group">
                                <label>Profile Image</label>
                                <input type="file" onChange={(e) => handleFileChange(e, 'profileImage')} />
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
                                <select value={tuteeProfile.location} onChange={(e) => setTuteeProfile({ ...tuteeProfile, location: e.target.value })}>
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
                        </>
                    )}

                    {role === 'TUTEE' && (
                        <>
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
                        </>
                    )}

                    {role === 'PARENT' && (
                        <>
                            {tuteeProfiles.map((profile, index) => (
                                <div key={index} className="input-group">
                                    <label>Child {index + 1} Grade</label>
                                    <input type="text" onChange={(e) => {
                                        const updatedProfiles = [...tuteeProfiles];
                                        updatedProfiles[index].grade = e.target.value;
                                        setTuteeProfiles(updatedProfiles);
                                    }} />
                                </div>
                            ))}
                            <button onClick={handleAddTuteeProfile}>Add Child Profile</button>
                        </>
                    )}
                    <button onClick={prevStep}>이전</button>
                    <button onClick={handleSignup}>회원가입</button>
                </>
            )}

            {error && <p className="error">{error}</p>}
        </div>
    );
};

export default Signup;
