
console.log("toast.js in");

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf.header"]').getAttribute('content');



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
document.getElementById("testBtn").addEventListener("click", () => {

  console.log(editor.getMarkdown());

})

// 저장 버튼
document.getElementById("toastSaveBtn").addEventListener("click", () => {
  
  const boardData = {
    writer : document.getElementById("toastWriter").value,
    title : document.getElementById("toastTitle").value
  }

  editorContentToServer(boardData).then(result => {

    if(result == "1"){
      location.href("/board/list");
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
async function editorContentToServer(boardData) {

  try {
    
    const url = "/board/toastPost";
    const config = {
      method : "post",
      headers : {
        [csrfHeader] : csrfToken,
        'Content-type' : 'application/json; charset=utf-8',
      }
    }

    const resp = await fetch(url, config);
    const result = resp.text();
    
    // result 의 값이 1이면 저장 성공하고 list 페이지로 보내기
    // 저장 실패시 alert로 실패 했다는 메세지 날리기
    return result;

  } catch (error) {
    console.log(error);
  }
  
}