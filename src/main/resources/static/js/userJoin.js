console.log("userJoin.js in");

const emailCheckBtn = document.getElementById("isEmailCheckBtn");
const nickCheckBtn = document.getElementById('isNickCheckBtn');
const signupBtn = document.getElementById('signupBtn');
const email = document.getElementById('e');
const nick = document.getElementById('n');
const pwd = document.getElementById('p');

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf.header"]').getAttribute('content');

// 유효성 검사 체크용 변수
let emailCheck = false;
let nickCheck = false;

// input 값 모음
const inputs = [email, nick, pwd];

// validation 함수
function joinValidation(inputs) {
    
    // 이메일 유효성
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    // 비밀번호 유효성 (영문 대소문자, 숫자, 특수문자 포함 8자리 이상)
    const regexPwd = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$/
    return regexEmail.test(email.value) && regexPwd.test(pwd.value);
    
}
// 중복검사를 완료하고 input 값을 변경 할 경우
inputs.forEach(input => {
    input.addEventListener('input', () => {

        // emailCheck = false;
        // nickCheck = false;
        
        const isValid = joinValidation(inputs);
        signupBtn.disabled = !(isValid && emailCheck && nickCheck);

    })
})

// 이메일 중복검사 버튼 클릭 이벤트
emailCheckBtn.addEventListener("click", () => {

    isDuplicationEmail(email.value).then(result => {
        console.log(result);
        const isValid = joinValidation(inputs);
        if(result == "1" && isValid){
            alert("사용 가능한 이메일 입니다.");
            emailCheck = true;
        }else{
            alert("사용 할 수 없는 이메일 입니다.");
            emailCheck = false;
        }
        
        signupBtn.disabled = !(isValid && emailCheck && nickCheck);

    })

});

// 닉네임 중복검사 버튼 클릭 이벤트
nickCheckBtn.addEventListener("click", () => {

    isDuplicationNick(nick.value).then(result => {
        const isValid = joinValidation(inputs);
        if(result == "1" && isValid){
            alert("사용 가능한 닉네임 입니다.");
            nickCheck = true;
        }else{
            alert("사용 할 수 없는 닉네임 입니다.");
            nickCheck = false;
        }

        signupBtn.disabled = !(isValid && emailCheck && nickCheck);
    })

})




// 비동기----------
// 이메일 중복검사
async function isDuplicationEmail(email) {
    try {
        
        const url = `/user/isCheckEmail/${email}`;
    
        const resp = await fetch(url);
        const result = await resp.text();

        return result;

    } catch (error) {
        console.log(error);
    }
    

}

// 닉네임 중복검사
async function isDuplicationNick(nick) {

    try {
        const url = `/user/isCheckNick/${nick}`;
    
        const resp = await fetch(url);
        const result = await resp.text();
        return result;
        
    } catch (error) {
        console.log(error);
    }


}



