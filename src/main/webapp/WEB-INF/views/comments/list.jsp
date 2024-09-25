<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>댓글 목록</title>
     <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/comments.css">
    <link rel="stylesheet" type="text/css" id="app-stylesheet" href="/resources/assets/css/main.css">
    <c:import url="../template/header_css.jsp"></c:import>
</head>
<body>
    <c:import url="../template/header_nav.jsp"></c:import>
    <div class="comments-container" style="margin-top: 200px;">
        <h2>댓글 목록</h2>
        <div id="commentForm">
            <c:choose>
                <c:when test="${empty sessionScope.member}">
                	<textarea id="commentContents" name="comment" placeholder="댓글을 입력하세요" class="comment-textarea" onclick="modalBtn.click()"></textarea>
                    <button id="modalBtn" type="button" class="md-btn md-btn--primary mb-3" data-bs-toggle="modal" data-bs-target="#exampleModal">로그인 후 댓글을 남겨주세요</button>
                </c:when>
                <c:otherwise>
                    <textarea id="commentContents" name="comment" placeholder="댓글을 입력하세요" class="comment-textarea"></textarea>

                    <button id="commentButton" type="button" class="comment-button mb-3" data-id="${param.boardNum}">댓글 남기기</button>
                </c:otherwise>
            </c:choose>
       
    </div>
    <div id="commentsList">

</div>
<div id="commentsObserverTarget" data-start-row="1" data-end-row="10" data-member-num="${sessionScope.member.memberNum}" data-board-num="${param.boardNum}" class="container"></div>


</div>    

<!-- 팝업 레이아웃 -->
<div id="profilePopup" class="profile-popup">
    <div class="profile-popup-content">
        <img src="">
        <p><b>작성자</b></p>
        <button id="followButton" class="like-button">팔로우</button>
        <button class="view-profile-button">프로필 보기</button>
    </div>
</div>





    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/comments/reply.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/comments/commentslist.js"></script>
</body>
</html>
