console.log("BoardDetail.js in")
console.log(bnoValue);

// listBtn 을 클릭하면 /board/list 로 이동
document.getElementById("listBtn").addEventListener("click", () => {
    location.href = "/board/list";
})

// DelBtn 을 클릭하면 /board/remove 로 이동 (bno 를 가지고 가야함.)
document.getElementById("delBtn").addEventListener("click", () => {
    location.href = "/board/remove?bno="+bnoValue;
})

// modBtn 을 클릭하면 title, content 만 readonly 풀기
document.getElementById("modBtn").addEventListener("click", () => {
    document.getElementById("t").readOnly = false;
    document.getElementById("c").readOnly = false;
    document.getElementById("t").focus(); // title 에 포커스 주기

    // 실제 submit 기능을 하는 버튼 추가
    let modBtn = document.createElement("button");
    modBtn.setAttribute("type", "submit");
    modBtn.setAttribute("id", "regBtn");
    modBtn.classList.add("btn", "btn-info");
    modBtn.innerText = "수정완료";

    // form 태그의 가장 마지막 요소로 추가
    document.getElementById("modForm").appendChild(modBtn);

    // 수정, 삭제 버튼 제거
    document.getElementById("modBtn").remove();
    document.getElementById("delBtn").remove();
})