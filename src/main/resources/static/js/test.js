    window.onload = function(){
const clickLikeUrl = "/assets/img/full.jpeg";
const emptyLikeUrl = "/assets/img/empty.png";

document.addEventListener("DOMContentLoaded", function() {
  let likeVal = document.getElementById('like_check').value;

    if (likeVal === 'true') {
        document.getElementById('likeImg').src = clickLikeUrl;
    } else if (likeVal === 'false') {
        document.getElementById('likeImg').src = emptyLikeUrl;
    }
});

document.getElementById('likeImg').addEventListener('click', function() {
    const boardId = /*[[${board.id}]]*/;
    console.log(boardId)
    const likeVal = document.getElementById('like_check').value;

    if (likeVal === 'true') {
        const conCheck = confirm("현재 게시물 추천을 취소하시겠습니까?");
        if (conCheck) {
            fetch(`/like/${boardId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
            })
                .then(response => response.json())
                .then(data => {
                    document.getElementById('likeImg').src = emptyLikeUrl;
                    location.reload();
                })
                .catch(error => {
                    alert(JSON.stringify(error));
                });
        }
    } else if (likeVal === 'false') {
        const conCheck = confirm("현재 게시물을 추천하시겠습니까?");
        if (conCheck) {
            fetch(`/like/${boardId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
            })
                .then(response => response.json())
                .then(data => {
                    document.getElementById('likeImg').src = clickLikeUrl;
                    location.reload();
                })
                .catch(error => {
                    alert(JSON.stringify(error));
                });
        }
    }
});

document.getElementById('loginCheck').addEventListener('click', function() {
    alert("로그인 후 이용할 수 있습니다.");
});
}