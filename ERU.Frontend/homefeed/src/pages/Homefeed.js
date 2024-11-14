import React, { useState, useEffect, useRef} from 'react';
import '../styles/App.css';

import Nav from '../components/Nav/Nav';
import Post from '../components/Post';

function HomeFeed() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [allLoaded, setAllLoaded] = useState(false);
  const loadedIdentifiers = useRef(new Set());

  const loadPosts = () => {
    if (loading || allLoaded) return;
    setLoading(true);

    fetch('http://127.0.0.1:7877/account/feed')
      .then(response => response.json())
      .then(data => {
        const uniquePosts = data.filter(item => !loadedIdentifiers.current.has(item.identifier));

        if (uniquePosts.length === 0) {
          setAllLoaded(true);
        } else {
          uniquePosts.forEach(item => loadedIdentifiers.current.add(item.identifier));

          const newPosts = uniquePosts.map(item => ({
            identifier: item.identifier,
            username: item.owning_user || 'unknown',
            caption: item.caption || 'No caption provided',
            imageUrl: item.content,
            profilePic: item.owning_picture || 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQdVgSoJ_6jouY4v5cmPt2mlTY7nS7gjMzng&s',
            comments: item.comments || []
          }));

          setPosts(prevPosts => [...prevPosts, ...newPosts]);
        }
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  };

  useEffect(() => {
    loadPosts();
  }, []);

  const feedRef = useRef(null);

  useEffect(() => {
    const handleScroll = () => {
      const feedElement = feedRef.current;

      if (feedElement) {
        const isBottom = feedElement.scrollHeight === feedElement.scrollTop + feedElement.clientHeight;

        if (isBottom) {
          loadPosts();
        }
      }
    };

    const feedElement = feedRef.current;
    if (feedElement) {
      feedElement.addEventListener('scroll', handleScroll);
    }

    return () => {
      if (feedElement) {
        feedElement.removeEventListener('scroll', handleScroll);
      }
    };
  }, [loading, allLoaded]);

  return (
    <div className="home-feed" ref={feedRef}>
      <Nav />
      {posts.map(post => (
        <Post
          key={post.identifier}
          identifier={post.identifier}
          username={post.username}
          imageUrl={post.imageUrl}
          caption={post.caption}
          profilePic={post.profilePic}
          comments={post.comments}
        />
      ))}
      {loading && <div className="loading">Loading more posts...</div>}
      {allLoaded && <div className="end-of-feed">You have reached the end of the feed</div>}
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
