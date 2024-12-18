import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import axios from 'axios';
import './Signup.css';
import LocationModal from '../modal/LocationModal.js';
import SchoolModal from '../modal/SchoolModal.js';

// Define subjects according to the enum in the backend
const subjectsList = [
    { value: "ELEMENTARY_MATH", label: "초등수학" },
    { value: "MIDDLE_MATH", label: "중등수학" },
    { value: "HIGH_MATH", label: "고등수학" },
    { value: "ELEMENTARY_ENGLISH", label: "초등영어" },
    { value: "MIDDLE_ENGLISH", label: "중등영어" },
    { value: "HIGH_ENGLISH", label: "고등영어" }
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
        imageUrl: null,
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
        personality: '',
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
            personality: '',
            description: '' 
        },
    ]);
    const [currentProfileIndex, setCurrentProfileIndex] = useState(0);
    const [locations, setLocations] = useState([]); 
    const [universities, setUniversities] = useState([]); 
    const [error, setError] = useState('');
    const [errors, setErrors] = useState('');
    const navigate = useNavigate();
    const [selectedLocation, setSelectedLocation] = useState(null);
    const [isLocationModalOpen, setIsLocationModalOpen] = useState(false);
    const [isSchoolModalOpen, setIsSchoolModalOpen] = useState(false);
    const [selectedSchool, setSelectedSchool] = useState(null);
    const [schoolType, setSchoolType] = useState("university");

    const phoneRegex = /^\d{2,3}\d{3,4}\d{4}$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,50}$/;

    const handleChange = (field, value) => {
        switch (field) {
            case 'nickname':
                setNickname(value);
                setErrors((prev) => ({ ...prev, nickname: value.length >= 2 && value.length <= 20 ? '' : '닉네임은 2~20자 이내여야 합니다.' }));
                break;
            case 'phoneNumber':
                setPhoneNumber(value);
                setErrors((prev) => ({ ...prev, phoneNumber: phoneRegex.test(value) ? '' : '핸드폰 번호 형식이 올바르지 않습니다. (예: 01000000000)' }));
                break;
            case 'password':
                setPassword(value);
                setErrors((prev) => ({ ...prev, password: passwordRegex.test(value) ? '' : '비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.' }));
                break;
            case 'passwordConfirm':
                setPasswordConfirm(value);
                setErrors((prev) => ({ ...prev, passwordConfirm: value === password ? '' : '비밀번호가 일치하지 않습니다.' }));
                break;
            case 'role':
                setRole(value);
                setErrors((prev) => ({ ...prev, role: value ? '' : '역할을 선택해주세요.' }));
                break;
            default:
                break;
        }
    };

    const handleProfileFieldChange = (field, value, profileType) => {
        console.log('handleProfileFieldChange called:', { field, value, profileType });
        if (profileType === "TUTOR") {
            setTutorProfile((prev) => {            
                console.log('Previous tutorProfile:', prev);
                const updatedProfile = { ...prev, [field]: value };
                console.log('Updated tutorProfile:', updatedProfile);
                return updatedProfile; 
            });

            // 필수 필드에 대한 검증
            if (field === "subjects") {
                setErrors((prev) => ({
                    ...prev,
                    subjects: value.length > 0 ? "" : "과목을 1개 이상 선택해주세요."
                }));
            }
            if (field === "location") {
                setErrors((prev) => ({
                    ...prev,
                    location: value ? "" : "지역을 선택해주세요."
                }));
            }
            if (field === "university") {
                setErrors((prev) => ({
                    ...prev,
                    university: value ? "" : "대학 정보를 선택해주세요."
                }));
            }
            if (field === "status") {
                setErrors((prev) => ({
                    ...prev,
                    status: value ? "" : "재학 상태를 선택해주세요."
                }));
            }
        } else if (profileType === "TUTEE") {
            setTuteeProfile((prev) => ({ ...prev, [field]: value }));

            // 필수 필드에 대한 검증
            if (field === "tuteeName") {
                setErrors((prev) => ({
                    ...prev,
                    tuteeName: value ? "" : "이름을 입력해주세요."
                }));
            }
            if (field === "gender") {
                setErrors((prev) => ({
                    ...prev,
                    gender: value ? "" : "성별을 선택해주세요."
                }));
            }
            if (field === "location") {
                setErrors((prev) => ({
                    ...prev,
                    location: value ? "" : "지역을 선택해주세요."
                }));
            }
            if (field === "subjects") {
                setErrors((prev) => ({
                    ...prev,
                    subjects: value.length > 0 ? "" : "과목을 1개 이상 선택해주세요."
                }));
            }
            if (field === "tuteeGrade") {
                setErrors((prev) => ({
                    ...prev,
                    tuteeGrade: value ? "" : "학년을 선택해주세요."
                }));
            }
        } else if (profileType === "PARENT") {
            setTuteeProfiles((prevProfiles) => {
                const updatedProfiles = [...prevProfiles]; // 불변성을 유지하며 배열 복사
                updatedProfiles[currentProfileIndex] = {
                    ...updatedProfiles[currentProfileIndex], // 현재 인덱스의 프로필 복사
                    [field]: value, // 변경된 필드만 업데이트
                };
                return updatedProfiles; // 업데이트된 배열 반환
            });

            // 필수 필드에 대한 검증
            if (field === "tuteeName") {
                setErrors((prev) => ({
                    ...prev,
                    tuteeName: value ? '' : "이름을 입력해주세요."
                }));
            }
            if (field === "gender") {
                setErrors((prev) => ({
                    ...prev,
                    gender: value ? "" : "성별을 선택해주세요."
                }));
            }
            if (field === "location") {
                setErrors((prev) => ({
                    ...prev,
                    location: value ? "" : "지역을 선택해주세요."
                }));
            }
            if (field === "subjects") {
                setErrors((prev) => ({
                    ...prev,
                    subjects: value.length > 0 ? "" : "과목을 1개 이상 선택해주세요."
                }));
            }
            if (field === "tuteeGrade") {
                setErrors((prev) => ({
                    ...prev,
                    tuteeGrade: value ? "" : "학년을 선택해주세요."
                }));
            }
        }
    };

    const validateStep = () => {
        const newErrors = {};
        if (step === 1) {
            if (!nickname || nickname.length < 2 || nickname.length > 20) newErrors.nickname = "닉네임은 2~20자 이내여야 합니다.";
            if (!phoneRegex.test(phoneNumber)) newErrors.phoneNumber = "핸드폰 번호 형식이 올바르지 않습니다. (예: 01000000000)";
        } else if (step === 2) {
            if (!passwordRegex.test(password)) newErrors.password = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.";
            if (password !== passwordConfirm) newErrors.passwordConfirm = "비밀번호가 일치하지 않습니다.";
            if (!role) newErrors.role = "역할을 선택해주세요.";
        } else if (step === 3) {
            if (role === "TUTOR") {
                if (!tutorProfile.subjects || tutorProfile.subjects.length === 0)
                    newErrors.subjects = "과목을 1개 이상 선택해주세요.";
                if (!tutorProfile.location)
                    newErrors.location = "지역을 선택해주세요.";
                if (!tutorProfile.university)
                    newErrors.university = "대학 정보를 선택해주세요.";
                if (!tutorProfile.status)
                    newErrors.status = "재학 상태를 선택해주세요.";
            } else if (role === "TUTEE") {
                if (!tuteeProfile.tuteeName)
                    newErrors.tuteeName = "이름을 입력해주세요.";
                if (!tuteeProfile.gender)
                    newErrors.gender = "성별을 선택해주세요.";
                if (!tuteeProfile.location)
                    newErrors.location = "지역을 선택해주세요.";
                if (!tuteeProfile.subjects || tuteeProfile.subjects.length === 0)
                    newErrors.subjects = "과목을 1개 이상 선택해주세요.";
                if (!tuteeProfile.tuteeGrade)
                    newErrors.tuteeGrade = "학년을 선택해주세요.";
            }
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };


    const 
    handleNextStep = () => {
        if (validateStep()) {
            setStep((prevStep) => prevStep + 1);
        }
    };

    const handleOpenLocationModal = () => setIsLocationModalOpen(true);
    const handleCloseLocationModal = () => setIsLocationModalOpen(false);
    
    const handleOpenSchoolModal = (type) => {
        setSchoolType(type);
        setIsSchoolModalOpen(true);
    };

    const handleCloseSchoolModal = () => setIsSchoolModalOpen(false);

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
            handleNextStep(); // Move to the next step only if validation is successful
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

    useEffect(() => {
        const fetchLocations = async (retry = false) => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/locations`, {
                    params: {
                        cd: '',
                        pg_yn: '0',
                    },
                });
                setLocations(response.data.content);
            } catch (err) {
                console.error('Failed to fetch locations:', err);
                if (
                    err.response?.data?.errCd === -401 &&
                    err.response?.data?.errMsg === "인증 정보가 존재하지 않습니다" &&
                    !retry
                ) {
                    console.log('Retrying fetchLocations...');
                    await fetchLocations(true);
                } else {
                    setError('지역 정보를 가져오는데 실패했습니다.');
                }
            }
        };
    
        const fetchUniversities = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/schools`, {
                    params: {
                        schoolName: '',
                        thisPage: '1',
                        perPage: '100',
                        gubun: 'univ_list',
                        schoolType1: '100323',
                        schoolType2: '100328',
                    },
                });
                setUniversities(response.data.content);
            } catch (err) {
                console.error('Failed to fetch universities:', err);
                setError('대학교 정보를 가져오는데 실패했습니다.');
            }
        };
    
        if (step === 3) {
            fetchLocations();
            if (role === 'TUTOR') {
                fetchUniversities();
            }
        }
    }, [step, role]);
    

    const handleSignup = async (e) => {
        e.preventDefault();
        if (validateStep()) {
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
        }
    };

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
        const newErrors = {};
    
        if (!currentProfile.tuteeName) {
            newErrors.tuteeName = "이름을 입력해주세요.";
        }
        if (!currentProfile.gender) {
            newErrors.gender = "성별을 선택해주세요.";
        }
        if (!currentProfile.location) {
            newErrors.location = "지역을 선택해주세요.";
        }
        if (currentProfile.subjects.length === 0) {
            newErrors.subjects = "과목을 1개 이상 선택해주세요.";
        }
        if (!currentProfile.tuteeGrade) {
            newErrors.tuteeGrade = "학년을 선택해주세요.";
        }
    
        if (Object.keys(newErrors).length > 0) {
            // 에러 상태 업데이트
            setErrors(newErrors);
            return;
        }
    
        // 새로운 학생 프로필 추가
        setTuteeProfiles([
            ...tuteeProfiles,
            { 
                tuteeName: '',
                gender: '', 
                location: '', 
                subjects: [], 
                description: '', 
                tuteeGrade: '' 
            }
        ]);
        setCurrentProfileIndex((prevIndex) => prevIndex + 1);
    
        // 오류 초기화
        setErrors({});
    };
    
    const handleRemoveTuteeProfile = (index) => {
        if (index === 0) {
            alert("첫 번째 학생 프로필은 삭제할 수 없습니다.");
            return;
        }
    
        setTuteeProfiles((prevProfiles) => {
            const updatedProfiles = prevProfiles.filter((_, i) => i !== index); // 선택한 프로필 삭제
            return updatedProfiles;
        });
    
        setCurrentProfileIndex((prevIndex) => {
            if (prevIndex >= index && prevIndex > 0) {
                return prevIndex - 1; // 현재 인덱스가 삭제된 인덱스 이후면 하나 줄임
            }
            return prevIndex;
        });
    };

    const handleNextProfile = () => {
        if (currentProfileIndex < tuteeProfiles.length - 1) {
            setCurrentProfileIndex((prevIndex) => prevIndex + 1);
        } else {
            alert("다음 프로필이 없습니다.");
        }
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
            console.log('Updating tutorProfile:', tutorProfile);
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

    const handleSaveLocation = (location) => {
        if (role === 'TUTOR') {
            setTutorProfile((prev) => ({ ...prev, location }));
        } else if (role === 'TUTEE') {
            setTuteeProfile((prev) => ({ ...prev, location }));
        } else if (role === 'PARENT') {
            setTuteeProfiles((prevProfiles) => {
                const updatedProfiles = [...prevProfiles];
                updatedProfiles[currentProfileIndex] = {
                    ...updatedProfiles[currentProfileIndex],
                    location,
                };
                return updatedProfiles;
        });
        }
        handleCloseLocationModal();
    };

    const handleSaveSchool = (school) => {
        if (role === "TUTOR") {
            setTutorProfile((prev) => ({
                ...prev,
                university: {
                    schoolName: school.schoolName,
                    seq: school.seq,
                },
            }));
        } else if (role === "TUTEE") {
            setTuteeProfile((prev) => ({
                ...prev,
                school: {
                    schoolName: school.schoolName,
                    seq: school.seq,
                },
            }));
        } else if (role === "PARENT") {
            setTuteeProfiles((prevProfiles) => {
                const updatedProfiles = [...prevProfiles];
                updatedProfiles[currentProfileIndex] = {
                    ...updatedProfiles[currentProfileIndex],
                    school: {
                        schoolName: school.schoolName,
                        seq: school.seq,
                    },
                };
                return updatedProfiles;
            });
        }
        handleCloseSchoolModal(); // 모달 닫기
    };  

    return (
        <div className="signup-container">
            <h2>회원가입</h2>

            {/* Step 1: Basic Information */}
            {step === 1 && (
                <>
                    <div className="input-group">
                        <label>닉네임 <span className="required">*</span></label>
                        <input 
                            type="text" 
                            value={nickname} 
                            onChange={(e) => handleChange('nickname', e.target.value)}     
                        />
                        {errors.nickname && <p className="error">{errors.nickname}</p>}
                    </div>

                    <div className="input-group">
                        <label>성별 <span className="required">*</span></label>
                        <select value={gender} onChange={(e) => setGender(e.target.value)} required>
                            <option value="">Select Gender</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </select>
                    </div>

                    <div className="input-group">
                        <label>핸드폰 번호 <span className="required">*</span></label>
                        <input 
                            type="text" 
                            value={phoneNumber} 
                            onChange={(e) => handleChange('phoneNumber', e.target.value)} 
                        />
                        {/* <button type="button" onClick={sendVerificationCode} className="verify-button">인증</button> */}
                        {errors.phoneNumber && <p className="error">{errors.phoneNumber}</p>}
                    </div>
                    <button onClick={handleNextStep}>다음</button>
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
                        <label>비밀번호 <span className="required">*</span></label>
                        <input 
                            type="password" 
                            value={password} 
                            onChange={(e) => handleChange('password', e.target.value)}
                        />
                        {errors.password && <p className="error">{errors.password}</p>}
                    </div>

                    <div className="input-group">
                        <label>비밀번호 확인 <span className="required">*</span></label>
                        <input 
                            type="password" 
                            value={passwordConfirm} 
                            onChange={(e) => handleChange('passwordConfirm', e.target.value)} 
                        />
                        {errors.passwordConfirm && (
                            <p className="error">{errors.passwordConfirm}</p>
                        )}
                    </div>

                    <div className="input-group">
                        <label>역할 <span className="required">*</span></label>
                        <select value={role} onChange={(e) => handleChange('role', e.target.value)} >
                            <option value="">Select Role</option>
                            <option value="TUTOR">Tutor</option>
                            <option value="TUTEE">Student</option>
                            <option value="PARENT">Parent</option>
                        </select>
                        {errors.role && <p className='error'>{errors.role}</p>}
                    </div>
                    <button onClick={() => setStep(1)}>이전</button>
                    <button onClick={handleNextStep}>다음</button>
                </>
            )}

            {/* Step 3: Profile Details Based on Role */}
            {step === 3 && (
                <>
                    <>
                    {role === 'TUTOR' && (
                        <>
                            <div className="input-group">
                                <label>프로필 이미지</label>
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
                                <label>과목 <span className="required">*</span></label>
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
                                {errors.subjects && <p className="error">{errors.subjects}</p>}
                            </div>
                            <div className="input-group">
                                <label>지역 <span className='required'>*</span></label>
                                <button onClick={handleOpenLocationModal}>
                                    {tutorProfile.location
                                        ? `${tutorProfile.location.sido.addr_name} > ${tutorProfile.location.sigungu.addr_name}`
                                        : '지역 선택'}
                                </button>
                                {errors.location && <p className="error">{errors.location}</p>}
                            </div>
                            <div className="input-group">
                                <label>대학 <span className='required'>*</span></label>
                                <button onClick={() => handleOpenSchoolModal("university")}>
                                    {tutorProfile.university.schoolName || "대학 선택"}
                                </button>
                                {errors.university && <p className="error">{errors.university}</p>}
                            </div>
                            <div className="input-group">
                                <label>재적 상태 <span className='required'>*</span></label>
                                <select 
                                    value={tutorProfile.status} 
                                    onChange={(e) => handleProfileFieldChange('status', e.target.value, 'TUTOR')}>
                                    <option value="">Select Enrollment Status</option>
                                    {enrollmentStatusList.map((status) => (
                                        <option key={status.value} value={status.value}>{status.label}</option>
                                    ))}
                                </select>
                                {errors.status && <p className="error">{errors.status}</p>}
                            </div>
                        </>
                    )}

                    {role === 'TUTEE' && (
                        <>
                            <div className="input-group">
                                <label>이름 <span className='required'>*</span></label>
                                <input
                                    type="text"
                                    value={tuteeProfile.tuteeName}
                                    onChange={(e) => handleProfileFieldChange('tuteeName', e.target.value, 'TUTEE')}
                                />
                                {errors.tuteeName && <p className="error">{errors.tuteeName}</p>}
                            </div>

                            <div className="input-group">
                                <label>성별 <span className='required'>*</span></label>
                                <select
                                    value={tuteeProfile.gender}
                                    onChange={(e) => handleProfileFieldChange('gender', e.target.value, 'TUTEE')}
                                >
                                    <option value="">Select Gender</option>
                                    <option value="MALE">Male</option>
                                    <option value="FEMALE">Female</option>
                                </select>
                                {errors.gender && <p className="error">{errors.gender}</p>}
                            </div>

                            <div className="input-group">
                                <label>지역 <span className='required'>*</span></label>
                                <button onClick={handleOpenLocationModal}>
                                    {tuteeProfile.location
                                        ? `${tuteeProfile.location.sido.addr_name} > ${tuteeProfile.location.sigungu.addr_name}`
                                        : '지역 선택'}
                                </button>
                                {errors.location && <p className="error">{errors.location}</p>}
                            </div>
                            
                            <div className="input-group">
                                <label>과목 <span className='required'>*</span></label>
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
                                {errors.subjects && <p className="error">{errors.subjects}</p>}
                            </div>
                            <div className="input-group">
                                <label>학년 <span className='required'>*</span></label>
                                <select
                                    value={tuteeProfile.tuteeGrade}
                                    onChange={(e) => handleProfileFieldChange( 'tuteeGrade', e.target.value, 'TUTEE')}
                                >
                                    <option value="">Select Grade</option>
                                    {tuteeGradeList.map((grade) => (
                                        <option key={grade.value} value={grade.value}>{grade.label}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="input-group">
                                <label>성격</label>
                                <input
                                    type="text"
                                    value={tuteeProfile.personality}
                                    onChange={(e) => handleProfileFieldChange( 'personality', e.target.value, 'TUTEE')}
                                />
                            </div>
                            <div className="input-group">
                                <label>소개</label>
                                <textarea
                                    value={tuteeProfile.description}
                                    onChange={(e) => handleProfileFieldChange( 'description', e.target.value, 'TUTEE')}
                                />
                            </div>
                        </>
                    )}

                    </>
                    {role === 'PARENT' && (
                        <>
                        <div className="signup-content">
                            <div className="form-container">
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                    <h3>학생 {currentProfileIndex + 1} 프로필</h3>
                                    {tuteeProfiles.length > 1 && (
                                        <button 
                                            className="remove-button" 
                                            onClick={() => handleRemoveTuteeProfile(currentProfileIndex)}
                                        >
                                            삭제
                                        </button>
                                    )}
                                </div>
                                <div className="input-group">
                                    <label>이름 <span className='required'>*</span></label>
                                    <input
                                        type="text"
                                        value={tuteeProfiles[currentProfileIndex]?.tuteeName || ''}
                                        onChange={(e) => handleProfileFieldChange('tuteeName', e.target.value, 'PARENT')}
                                    />
                                    {errors.tuteeName && <p className="error">{errors.tuteeName}</p>}
                                </div>
                                <div className="input-group">
                                    <label>성별 <span className='required'>*</span></label>
                                    <select
                                        value={tuteeProfiles[currentProfileIndex]?.gender || ''}
                                        onChange={(e) => handleProfileFieldChange('gender', e.target.value, 'PARENT')}
                                    >
                                        <option value="">Select Gender</option>
                                        <option value="MALE">Male</option>
                                        <option value="FEMALE">Female</option>
                                    </select>
                                    {errors.gender && <p className="error">{errors.gender}</p>}
                                </div>

                                <div className="input-group">
                                    <label>지역 <span className='required'>*</span></label>
                                    <button onClick={handleOpenLocationModal}>
                                        {tuteeProfiles[currentProfileIndex]?.location
                                            ? `${tuteeProfiles[currentProfileIndex]?.location.sido.addr_name} > ${tuteeProfiles[currentProfileIndex]?.location.sigungu.addr_name}`
                                            : '지역 선택'}
                                    </button>
                                    {errors.location && <p className="error">{errors.location}</p>}
                                </div>    

                                <div className="input-group">
                                    <label>과목 <span className='required'>*</span></label>
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
                                    {errors.subject && <p className="error">{errors.subject}</p>}
                                </div>

                                <div className="input-group">
                                    <label>학년 <span className='required'>*</span></label>
                                    <select
                                        value={tuteeProfiles[currentProfileIndex].tuteeGrade}
                                        onChange={(e) => handleProfileFieldChange('tuteeGrade', e.target.value, 'PARENT')}
                                    >
                                        <option value="">Select Grade</option>
                                        {tuteeGradeList.map((grade) => (
                                            <option key={grade.value} value={grade.value}>
                                                {grade.label}
                                            </option>
                                        ))}
                                    </select>
                                    {errors.tuteeGrade && <p className="error">{errors.tuteeGrade}</p>}
                                </div>
                                <div className="input-group">
                                    <label>성격</label>
                                    <input
                                        type="text"
                                        value={tuteeProfiles[currentProfileIndex]?.personality || ''}
                                        onChange={(e) => handleProfileFieldChange('personality', e.target.value, 'PARENT')}
                                    />
                                </div>
                                <div className="input-group">
                                    <label>소개</label>
                                    <textarea
                                        value={tuteeProfiles[currentProfileIndex].description}
                                        onChange={(e) => handleProfileChange('description', e.target.value, 'PARENT')}
                                    />
                                </div>    
                            </div>
                        </div>                                   
                        </>
                    )}
                    {isLocationModalOpen && (
                        <LocationModal onClose={handleCloseLocationModal} onSave={handleSaveLocation}/>
                    )}
                    {isSchoolModalOpen && (
                        <SchoolModal onClose={handleCloseSchoolModal} onSave={handleSaveSchool} schoolType={schoolType} />
                    )}

                    <div className="button-group">
                        {role === 'PARENT' && currentProfileIndex > 0 ? (
                            // 첫 번째 학생 프로필이 아닐 때 이전 프로필로 이동
                            <button onClick={handlePreviousProfile}>이전</button>
                        ) : (
                            // 첫 번째 학생 프로필일 때 이전 스텝으로 이동
                            <button onClick={() => setStep(2)}>이전</button>
                        )}
                        {currentProfileIndex < tuteeProfiles.length - 1 && (
                        <button onClick={handleNextProfile}>다음 프로필</button>
                        )}
                        {role === 'PARENT' && (
                            <button onClick={handleAddTuteeProfile}>프로필 추가</button>
                        )}
                        <button onClick={handleSignup}>회원가입</button>
                    </div>   
                </>
                )}
            {errors.server && <p className="error">{errors.server}</p>}
        </div>
    );
};

export default Signup;