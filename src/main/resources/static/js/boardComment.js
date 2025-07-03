console.log("boardComment.js in");

const cmtWriter = document.getElementById("cmtWriter");
const cmtText = document.getElementById("cmtText");
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf.header"]').getAttribute('content');



// 일반 함수 --------------------

// 댓글 등록 버튼
document.getElementById("cmtAddBtn").addEventListener("click", () => {

    const commentData = {

        bno : bnoValue,
        writer : cmtWriter.innerText,
        content : cmtText.value

    }

    commentPostToServer(commentData).then(result => {
        console.log(commentData);
        if(result == "1"){
            alert("댓글 등록 성공");
        }else{
            alert("댓글 등록 실패");
        }
        cmtText.value = '';

        spreadCommentList(bnoValue);
        cmtText.focus();
    })


})

// 화면 출력 함수
function spreadCommentList(bnoValue, page=1){
    commentGetListFromServer(bnoValue, page).then(result => {
        console.log(result);
        console.log(result.list);

        const ul = document.getElementById("cmtListArea");

        if(result.list.length > 0){
            if(page == 1){
                ul.innerHTML = '';
            }
            for(let cvo of result.list) {
                let li = `<li class="list-group-item" data-cno="${cvo.cno}">`;
                    li += `<div class="mb-3">`;
                    li += `<div class="fw-bold">${cvo.writer}</div>`;
                    li += `${cvo.content}`;
                    li += `</div>`;
                    li += `<span class="badge rounded-pill text-bg-info">${cvo.regDate}</span>`;
                    if(cvo.writer == userEmail){
                        li += `<button
                        type="button"
                        class="btn btn-outline-primary mod btn-sm"
                        data-bs-toggle="modal" data-bs-target="#myModal"
                        >
                        수정
                        </button>`; // 수정버튼
                        li += `<button type="button" class="btn btn-outline-warning del btn-sm">삭제</button>`; // 삭제버튼
                    }
                    li += `</li>`;

                ul.innerHTML += li;
            }
        // 더보기 버튼 숨김 여부 체크
        let moreBtn = document.getElementById('moreBtn');
        // 더보기 버튼이 표시되는 조건
        // ph => realEndPage > pageNo => 더보기 표시
        // 현재 페이지가 전체 페이지보다 작으면 표시

            if(result.pageNo < result.totalPage) {
                moreBtn.style.visibility = "visible";
                // 페이지 1 증가 후 다시 data-page 로 달기
                moreBtn.dataset.page = page + 1;
                
            }else{
                // 현재 페이지가 전체 페이지보다 크다면
                moreBtn.style.visibility = "hidden";
            }

        }else{
            // 댓글이 없을경우
            ul.innerHTML = `<li class="list-group-item">Comment List Empty</li>`;
        }
        

    })
}

document.addEventListener("click", (e) => {
    
    if(e.target.id == 'moreBtn'){
        spreadCommentList(bnoValue, parseInt(e.target.dataset.page));
    }

    let li = e.target.closest('li');

    if(e.target.classList.contains('mod')){
        // 수정
        // nextSibling : 같은 부모의 형제 찾기
        let cmtText = li.querySelector('.fw-bold').nextSibling;
        let cmtWriter = li.querySelector('.fw-bold').innerText;
        console.log(cmtText);
        // 객체의 값을 스트링 형태로 선택된 것이 아님. => nodeValue
        document.getElementById('cmtTextMod').value = cmtText.nodeValue;
        document.getElementById('cno-writer').innerText = cmtWriter;
        // cmtModBtn (수정버튼) 에 data-cno 값을 속성으로 추가
        document.getElementById('cmtModBtn').setAttribute('data-cno', li.dataset.cno);
    }

    if(e.target.id == 'cmtModBtn'){
        // 모달 수정 버튼
        let cmtModData = {
            cno : e.target.dataset.cno,
            content : document.getElementById('cmtTextMod').value
        }

        commentModifyToServer(cmtModData).then(result => {
            if(result == "1"){
                alert('댓굴 수정 성공');
            }else{
                alert('댓글 수정 실패');
            }

            spreadCommentList(bnoValue);
            document.querySelector('.btn-close').click();
        })

    }

    if(e.target.classList.contains('del')){
        // 삭제
        commentDeleteToServer(li.dataset.cno).then(result => {
            if(result == "1"){
                alert('댓글 삭제 성공');
            }else{
                alert('댓글 삭제 실패');
            }
            spreadCommentList(bnoValue)
        })
    }
})


// 비동기 함수 ------------------

// 댓글 등록
async function commentPostToServer(commentData) {
    
    try {
        
        const url = "/comment/post"
        const config = {
            method : "post",
            headers : {
                'Content-Type' : 'application/json; charset=utf-8',
                [csrfHeader] : csrfToken
            },
            body : JSON.stringify(commentData)
        }
        const resp = await fetch(url, config);
        const result = await resp.text();
        
        return result;
        
    } catch (error) {
        console.log(error);
    }
    
    
}
// 댓글 출력
async function commentGetListFromServer(bnoValue, page) {
    
    try {
        const url = `/comment/list/${bnoValue}/${page}`;
        const resp = await fetch(url);
        const result = await resp.json();

        return result;

    } catch (error) {
        console.log(error);
    }

}
// 댓글 삭제
async function commentDeleteToServer(cnoValue) {
    
    try {
        
        const url = `/comment/delete/${cnoValue}`
        const resp = await fetch(url, {method : 'delete', headers : {[csrfHeader] : csrfToken}});
        const result = resp.text();

        return result;

    } catch (error) {
        console.log(error);
    }

}

// 댓글 수정
async function commentModifyToServer(cmtModData) {
    
    try {
        
        const url = `/comment/modify`;
        const config = {
            method : 'put',
            headers : {
                'Content-type' : 'application/json; charset=utf-8',
                [csrfHeader] : csrfToken
            },
            body : JSON.stringify(cmtModData)
        }

        const resp = await fetch(url, config);
        const result = await resp.text();

        return result;

    } catch (error) {
        console.log(error);
    }

}