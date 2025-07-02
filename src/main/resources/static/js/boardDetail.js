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
    document.getElementById("t").focus(); // title 에 포커스 주기
    document.getElementById("toast-content").remove();
    document.getElementById("editor").style.display = "block";
    
    

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

    // file 업로드 버튼 비활성화 해제
    document.getElementById("trigger").disabled = false;

    // file-x (클래스) 버튼의 스타일 변경
    let fileDelBtn = document.querySelectorAll('.file-x');
    fileDelBtn.forEach(btn => {
        btn.style.visibility = "visible";
        // file-x 버튼을 클릭하면 비동기로 uuid 를 보내서 DB 상 파일 삭제
        btn.addEventListener("click", (e) => {

            let uuid = btn.dataset.uuid;

            // 비동기 전송
            removeFileToServer(uuid).then(result => {
                console.log("removeFile result =>"+result);
                if(result == "1"){
                    alert('파일 삭제 성공');
                    e.target.closest('li').remove();
                }else{
                    alert('파일 삭제 실패');
                }
            })
        })
    })
})

document.getElementById('trigger').addEventListener("click", () => {
    document.getElementById("file").click();
})

// 파일 삭제
async function removeFileToServer(uuid) {

    try {
        
        const url = `/board/file/${uuid}`;
        const config = {
            method : 'delete'
        }

        const resp = await fetch(url, config);
        const result = await resp.text();

        return result;

    } catch (error) {
        console.log(error);
    }
    
}

// 실행 파일 막기, 10MB 이상 파일 사이즈 제한

const regExp = new RegExp("\.(exe|sh|bat|jar|dll|msi)$");
const maxSize = 1024*1024*10;


function fileValid(fileName, fileSize){ 

    if(regExp.test(fileName)){
        return 0;
    }else if(fileSize > maxSize){
        return 0;
    }else{
        return 1;
    }

};

document.addEventListener('change', (e) => {
    
    if(e.target.id == 'file'){
        // multiple : 파일 객체 배열
        const fileObject = document.getElementById('file').files;
        console.log(fileObject);

        // 등록버튼 비활성화 풀기
        document.getElementById('regBtn').disabled = false;

        const div = document.getElementById('fileList');
        // 이전 잘못된 파일들 제거
        div.innerHTML = "";

        let isOk = 1; // valid 검증을 통과했는지의 여부

        // 파일이 여러개 입력가능 => 모두 다 valid 를 통과해야 등록가능
        // 모든 결과가 1이어야 register 버튼을 활성화 => 누적곱을 통해 검증
    
        let ul = `<ul class="list-group list-group-flush">`;
            // 개별 파일에 대한 검증 / 결과표시
            for(let file of fileObject){
                let validResult = fileValid(file.name, file.size); // 개별결과
                isOk *= validResult; // 전체누적
                ul += `<li class="list-group-item>`;
                ul += `<div class="mb-3">`;
                ul += `${validResult ? '<div class="fw-bold text-success">업로드 가능' : '<div class="fw-bold text-danger">업로드 불가능'}`;
                if(file.size > maxSize){
                    ul += `<span class="badge text-bg-${validResult ? 'success' : 'danger'}">${(file.size/(1024*1024)).toFixed(2)} MB</span>`;
                }
                ul += `</div>`;
                ul += `${file.name}`;
                ul += `</div>`;
                ul += `<span class="badge text-bg-${validResult ? 'success' : 'danger'}">${file.size} Bytes</span>`;
                ul += `</li>`;
            }
            ul+= `</ul>`
    
            div.innerHTML = ul;

            if(isOk == 0){
                // 하나라도 검증을 통과하지 못한 파일이 있다면 regBtn 비활성화
                document.getElementById('registerBtn').disabled = true;
            }

    }

})

// toast 내부 내용 수정하기
console.log(toastContent);
const turndownService = new TurndownService();
const markdown = turndownService.turndown(toastContent);
console.log(markdown);

// toast editor 수정시 나타나게 할 editor 만들어놓기.
const editor = new toastui.Editor({
    el: document.querySelector('#editor'),
    height : '500px',
    initialValue : markdown,
    initialEditType : 'markdown',
    previewStyle : 'vertical',
    language : 'ko-KR',
    placeholder : '내용을 입력해주세요.',


    // addImageBlobHook 사용해서 toast 내부의 이미지를 폴더에 저장하기.
    hooks : {
    addImageBlobHook(blob, callback){
        console.log(blob);
        console.log(callback);

        renderingImage(blob).then(result => {
        console.log(result);
        callback(result);
        })

    }

    }

});

// regBtn 을 눌렀을 경우 비동기로 content 의 내용을 수정
document.getElementById("modForm").addEventListener("submit", async (e) => {

        e.preventDefault(); // 비동기가 실행하기전 submit을 막기

        const contentData = {
            bno : bnoValue,
            content : editor.getHTML()
          }
    
          console.log(contentData);
        
          await editorContentModifyToServer(contentData).then(result => {
        
            if(result == "1"){
              alert("저장 성공");
              // location.href("/board/list");
              e.target.submit(); // 비동기 실행이 끝나면 form제출
            }else{
              alert("저장 실패...");
            }
        
          })


})

async function editorContentModifyToServer(contentData) {

    try {
      
      const url = "/board/toastModify";
      const config = {
        method : "put",
        headers : {
          [csrfHeader] : csrfToken,
          'Content-type' : 'application/json; charset=utf-8',
        },
        body : JSON.stringify(contentData)
      }
  
      const resp = await fetch(url, config);
      const result = await resp.text();
      
      // result 의 값이 1이면 저장 성공하고 list 페이지로 보내기
      // 저장 실패시 alert로 실패 했다는 메세지 날리기
      return result;
  
    } catch (error) {
      console.log(error);
    }
    
  }



