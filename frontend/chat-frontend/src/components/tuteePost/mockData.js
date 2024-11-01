import { faker } from '@faker-js/faker';

// 100개의 더미 데이터 생성
const generateMockPostings = () => {
    return Array.from({ length: 100 }, (_, index) => ({
        postingId: index + 1,
        studentGrade: faker.helpers.arrayElement(["중학교 1학년", "중학교 2학년", "고등학교 1학년", "고등학교 2학년"]),
        studentSchool: faker.company.name(),
        personality: faker.helpers.arrayElement(["성실하고 긍정적", "책임감이 강함", "논리적 사고", "창의적"]),
        possibleSchedule: faker.helpers.arrayElement(["주말 오후", "평일 저녁", "주중 오전", "주말 오전"]),
        level: faker.helpers.arrayElement(["초급", "중급", "고급"]),
        fee: `${faker.number.int({ min: 50, max: 200 })},000원`,  // faker.number.int로 변경
        memberNickname: faker.internet.userName(),
        memberId: faker.number.int({ min: 100, max: 999 }),  // faker.number.int로 변경
    }));
};

// 생성한 목데이터를 export
const mockPostings = generateMockPostings();
export default mockPostings;
