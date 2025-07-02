console.log("toastView.js in");

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf.header"]').getAttribute('content');

// toast viewer

const viewer = new toastui.Editor({
  el : document.querySelector('#viewer'),
  height : '500px',
  viewer : true,
  initialValue : '# hello',
});