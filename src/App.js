import React, { useState, useEffect }  from 'react';
import './App.css';


function Post({ username, imageUrl, caption, profilePic }) { /* Displays each post*/

  /*Manages comments*/

  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [isCommentInputVisible, setCommentInputVisible] = useState(false);

  /*Inline styles for parts of post layout*/
  
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

  /*Handles adding comments*/
  const handleAddComment = () => {
    if (newComment.trim() !== "") { // Prevents empty comments
      setComments([...comments, newComment]); // Add new comment to comments array
      setNewComment(""); // Clear input after adding
    }
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
          <img src="https://img.icons8.com/?size=100&id=82788&format=png&color=000000" alt="Like" style={postIconStyle} />
          {/* Comment icon */}
          <img 
            src="https://cdn-icons-png.flaticon.com/512/456/456214.png" 
            alt="Comment" 
            style={postIconStyle} 
            onClick={() => setCommentInputVisible(!isCommentInputVisible)} 
          />
          <img src="https://img.icons8.com/?size=100&id=18765&format=png&color=000000" alt="Save" style={postIconStyle} />
        </div>
        <p style={postCaptionStyle}>
          <strong>{username}</strong> {caption}
        </p>

        {/* Comment Section */}
        <div className="post-comments">
          {comments.map((comment, index) => (
            <p key={index}><strong>{username}</strong> {comment}</p>
          ))}
        </div>

        {/* Renders comment input */}
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

/*displays posts with infinite scrolling function*/
function HomeFeed() {
  const [posts, setPosts] = useState([ /*fill this with API call */
    {
      username: 'user',
      caption: 'Loving this view!',
      imageUrl: 'https://images.unsplash.com/photo-1584432743501-7d5c27a39189?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8bmljZSUyMHZpZXd8ZW58MHx8MHx8fDA%3D',
      profilePic: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
    },
    {
      username: 'user',
      caption: 'Had a great time hiking!',
      imageUrl: 'https://i.natgeofe.com/n/7afda449-1780-4938-8342-2abe32326c86/Montblanchike.jpg',
      profilePic: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
    },
  ]);

  /* scroll event listener for infinite scrolling */
  useEffect(() => {
    const handleScroll = () => {
      if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 500) {
        loadMorePosts();
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const loadMorePosts = () => { /* fill this with API call */
    const newPosts = [
      {
        username: 'user',
        caption: 'Ice cream time!',
        imageUrl: 'https://www.machineryworld.com/wp-content/uploads/2022/02/Ice-cream-lovers.jpg',
        profilePic: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
      },
      {
        username: 'user',
        caption: 'Just chilling',
        imageUrl: 'https://images.ctfassets.net/i3kf1olze1gn/3pFHM6fHHgCqqMBjMmDpPc/05dc4091da335778e3586f516e6c49f9/relaxing_beach_hero.jpg',
        profilePic: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
      },
    ];
    setPosts((prevPosts) => [...prevPosts, ...newPosts]); /*adds new posts to existing posts*/
  };

  return (
    <div className="home-feed">
      {posts.map((post, index) => (
        <Post
          key={index}
          username={post.username}
          imageUrl={post.imageUrl}
          caption={post.caption}
          profilePic={post.profilePic}
        />
      ))}
    </div>
  );
}

function App() {
  return (
    <div className="App">
      <HomeFeed />
    </div>
  );
}

export default App;
