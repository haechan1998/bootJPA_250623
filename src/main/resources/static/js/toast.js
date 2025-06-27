
console.log("toast.js in");

const editor = new toastui.Editor({
  el: document.querySelector('#editor'),
  height : '500px',
  initialValue : '',
  initialEditType : 'markdown',
  previewStyle : 'vertical',
  language : 'ko-KR',
  placeholder : '내용을 입력해주세요.',


  // addImageBlobHook 사용해서 toast 내부의 이미지를 폴더에 저장하기.
  hooks: {
    addImageBlobHook(blob, callback){
      console.log(blob);
      console.log(callback);
    }

  }

});