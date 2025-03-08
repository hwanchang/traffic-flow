import http from 'k6/http';
import {check, sleep} from 'k6';
import {SharedArray} from 'k6/data';
import papaparse from 'https://cdnjs.cloudflare.com/ajax/libs/PapaParse/5.3.0/papaparse.min.js';

// ✅ CSV에서 유저 데이터 불러오기
const users = new SharedArray('users', function () {
    return papaparse.parse(open('./users.csv'), {header: true}).data;
});

// ✅ k6 부하 테스트 설정
export let options = {
    vus: 50, // 동시 사용자 50명
    duration: '30s', // 30초 동안 테스트 실행
};

export default function () {
    let user = users[__VU % users.length]; // VU마다 다른 유저 사용

    // 🔹 로그인 API 요청 (쿠키 저장됨)
    let loginRes = http.post(
        'http://localhost:8080/api/v1/auth/login',
        JSON.stringify({email: user.email, password: user.password}),
        {headers: {'Content-Type': 'application/json'}}
    );

    // 🔹 로그인 성공 시 HTTP-Only 쿠키 자동 저장
    let jar = http.cookieJar();
    jar.set('http://localhost:8080', loginRes.cookies);

    // 🔹 쿠키 유지한 상태로 API 요청
    let res = http.post('http://localhost:8080/api/v1/loan-comparisons', {jar});

    check(res, {'API response is 200': (r) => r.status === 200});

    sleep(1);
}
