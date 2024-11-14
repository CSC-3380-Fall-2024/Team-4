import React, { useState, useEffect, useRef} from 'react';
import '../styles/App.css';

function Post({ identifier, username, imageUrl, caption, profilePic, comments: initialComments }) {
    const [comments, setComments] = useState(initialComments);
    const [newComment, setNewComment] = useState("");
    const [isCommentInputVisible, setCommentInputVisible] = useState(false);
  
    const postStyle = {
      backgroundColor: 'white',
      marginBottom: '30px',
      borderRadius: '8px',
      border: '1px solid #dbdbdb',
      textAlign: 'left',
      overflow: 'hidden',
    };
  
    const postHeaderStyle = {
      display: 'flex',
      alignItems: 'center',
      padding: '10px',
      borderBottom: '1px solid #dbdbdb',
    };
  
    const postProfilePicStyle = {
      width: '40px',
      height: '40px',
      borderRadius: '50%',
      marginRight: '10px',
    };
  
    const postImageStyle = {
      width: '100%',
      height: 'auto',
      display: 'block',
      borderBottom: '1px solid #dbdbdb',
    };
  
    const postActionsStyle = {
      display: 'flex',
      padding: '10px',
      justifyContent: 'flex-start',
    };
  
    const postIconStyle = {
      width: '25px',
      height: '25px',
      marginRight: '15px',
      cursor: 'pointer',
    };
  
    const postCaptionStyle = {
      padding: '10px',
      fontSize: '14px',
      color: '#262626',
    };
  
    const handleAddComment = () => {
      if (newComment.trim() === "") return;
  
      let commentBody = {
        account: "dwyx",
        content: newComment,
        likes: []
      };
  
      fetch(`http://127.0.0.1:7877/post/${identifier}/comment`, {
        method: 'POST',
        headers: {
          'Content-Type':'application/json',
        },
        body: JSON.stringify(commentBody),
      })
        .then(response => response.json())
        .then(data => {
          setComments([...comments, commentBody]);
          setNewComment("");
        })
        .catch(error => {
          console.error("Error adding comment:", error);
        });
    };
  
    return (
      <div style={postStyle}>
        <div style={postHeaderStyle}>
          <img src={profilePic} alt={`${username}'s profile`} style={postProfilePicStyle} />
          <h3>{username}</h3>
        </div>
        <div>
          <img src={imageUrl} alt="Post" style={postImageStyle} />
          <div style={postActionsStyle}>
            <img src="./like.png" alt="Like" style={postIconStyle} />
            <img
              src="./comment.png"
              alt="Comment"
              style={postIconStyle}
              onClick={() => setCommentInputVisible(!isCommentInputVisible)}
            />
            <img src="https://img.icons8.com/?size=100&id=18765&format=png&color=000000" alt="Save" style={postIconStyle} />
          </div>
          <p style={postCaptionStyle}>
            <strong>{username}</strong> {caption}
          </p>
          <div className="post-comments">
            {comments.map((comment, index) => (
              <p key={index}><strong>{comment.account}</strong> {comment.content}</p>
            ))}
          </div>
  
          {isCommentInputVisible && (
            <div className="post-add-comment">
              <input
                type="text"
                placeholder="Add a comment..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
              />
              <button onClick={handleAddComment}>Comment</button>
            </div>
          )}
        </div>
      </div>
    );
}

  export default Post;