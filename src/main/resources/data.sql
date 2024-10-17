-- 첫 번째 회원(Member 1: 튜티)
INSERT INTO member (nickname, gender, phoneNumber, password, passwordConfirm, role, createdAt, updatedAt)
VALUES ('member1_nickname', 'MALE', '010-1234-5678', 'password123', 'password123', 'TUTEE', NOW(), NOW());

-- 두 번째 회원(Member 2: 튜터)
INSERT INTO member (nickname, gender, phoneNumber, password, passwordConfirm, role, createdAt, updatedAt)
VALUES ('member2_nickname', 'FEMALE', '010-9876-5432', 'password456', 'password456', 'TUTOR', NOW(), NOW());

-- 튜터 프로필 생성
INSERT INTO tutorProfile (memberId, location, university, status, createdAt, updatedAt)
VALUES ((SELECT id FROM member WHERE nickname = 'member2_nickname'), 'SEOUL', 'SEOUL_UNIVERSITY', 'ENROLLED', NOW(), NOW());

-- 튜터가 가르치는 과목 추가 (예: 수학, 과학)
INSERT INTO tutorSubjects (tutorProfileId, subjects)
VALUES ((SELECT id FROM tutorProfile WHERE memberId = (SELECT id FROM member WHERE nickname = 'member2_nickname')), 'MIDDLE_MATH'),
       ((SELECT id FROM tutorProfile WHERE memberId = (SELECT id FROM member WHERE nickname = 'member2_nickname')), 'MIDDLE_ENGLISH');

-- 튜티 프로필 생성
INSERT INTO tuteeProfile (memberId, location, grade, description, createdAt, updatedAt)
VALUES ((SELECT id FROM member WHERE nickname = 'member1_nickname'), 'BUSAN', 'HIGH_1', 'Looking for a tutor in math and science.', NOW(), NOW());

-- 튜티가 필요한 과목 추가 (예: 수학, 과학)
INSERT INTO tuteeSubjects (tuteeProfileId, subjectsNeeded)
VALUES ((SELECT id FROM tuteeProfile WHERE memberId = (SELECT id FROM member WHERE nickname = 'member1_nickname')), 'MIDDLE_MATH'),
       ((SELECT id FROM tuteeProfile WHERE memberId = (SELECT id FROM member WHERE nickname = 'member1_nickname')), 'MIDDLE_ENGLISH');



-- 첫 번째 게시물 (튜티 게시물) 삽입
INSERT INTO tuteePost (memberId, grade, tuteeId, description, school, personality, type, possibleSchedule, level, fee, createdAt, updatedAt)
VALUES
    (1, 'HIGH_1', 1, '수학 과외를 받고 싶습니다. 주 2회, 1시간씩 진행했으면 좋겠습니다.', '서울고등학교', '적극적', 'ON_LINE', '월, 수 오후 4시 이후', '중급', 60, NOW(), NOW());
