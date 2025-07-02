
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf.header"]').getAttribute('content');
console.log("boardRegister.js in");

document.getElementById('trigger').addEventListener("click", () => {
    document.getElementById("file").click();
})

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


console.log("toast.js in");

// toast editor
const editor = new toastui.Editor({
  el: document.querySelector('#editor'),
  height : '500px',
  initialValue : '',
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





// 마크다운 확인용
// document.getElementById("testBtn").addEventListener("click", () => {

//   const boardData = {
//     title : document.getElementById("toastTitle").value,
//     writer : document.getElementById("toastWriter").value,
//     content : editor.getMarkdown()
//   }
//   console.log(boardData); 
//   console.log(editor.getMarkdown());
//   console.log(editor.getHTML());

// })

// 저장 버튼
document.getElementById("regBtn").addEventListener("click", () => {
    
  const boardData = {
    title : document.getElementById('t').value,
    writer : document.getElementById('w').value,
    content : editor.getHTML()
  }
  console.log(boardData);

  const fileData = document.getElementById('file').files;
  console.log(">>> fileData > ",fileData);

  editorContentToServer(boardData, fileData).then(result => {

    if(result == "1"){
      alert("저장 성공");
      location.href = "/board/list";

    }else{
      alert("저장 실패...");
    }

  })


})
async function renderingImage(blob) {

  try {
    const formData = new FormData();
    formData.append('image', blob);

    const url = "/board/toast";
        const config = {
          method : "post",
          headers : {
            [csrfHeader] : csrfToken
          },
          body : formData
        }

        const resp = await fetch(url, config);
        const result = resp.text();
        return result;

  } catch (error) {
    console.log("image 업로드 실패");
        
        alert("이미지 업로드를 실패했습니다.");
  }
  
}

// 비동기 데이터로 editor 의 내용 보내기
async function editorContentToServer(boardData, fileData) {

  try {
    const formData = new FormData();
    formData.append('boardDTO', new Blob([JSON.stringify(boardData)], { type: 'application/json' }));

    for(let i = 0; i < fileData.length; i++){ // size를 length로
        formData.append('files', fileData[i]); // fileData[i]?
    }

    const url = "/board/register";
    const config = {
      method : "post",
      headers : {
        [csrfHeader] : csrfToken,
      },
      body : formData
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