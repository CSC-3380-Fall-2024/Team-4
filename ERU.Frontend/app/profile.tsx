import React, { useState, useEffect, useRef } from 'react';
import './profile.css';
import './styles/App.css';

import Post from './components/Post';
import Comment from './components/Comment';

interface PostData {
  identifier: string;
  username: string;
  caption: string;
  imageUrl: string;
  profilePic: string;
  comments: Comment[];
}

interface AccountData {
  account_id: string;
  name: string;
  display_name: string;
  external_auths: Map<string, string>;
  email: string;
  banned: boolean;
  profile_picture: string;
}

function ProfilePage() {
  const [posts, setPosts] = useState<PostData[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [allLoaded, setAllLoaded] = useState<boolean>(false);
  const loadedIdentifiers = useRef<Set<string>>(new Set());
  const [user, setUser] = useState<AccountData>();

  const loadPosts = () => {
    const AccountId = "Tamely"; // Hardcoded atm. TODO: Fetch from Login.

    if (AccountId == null || loading || allLoaded) return;
    setLoading(true);

    fetch(`http://127.0.0.1:7877/account/${AccountId}`)
    .then((response) => response.json())
    .then((data: AccountData) => {
      setUser(data);
    })
    .catch((error) => {
      console.error('Error fetching user data:', error);
    });

    

    fetch(`http://127.0.0.1:7877/account/${AccountId}/feed`)
      .then((response) => response.json())
      .then((data: Array<any>) => {
        const uniquePosts = data.filter(
          (item) => !loadedIdentifiers.current.has(item.identifier)
        );

        if (uniquePosts.length === 0) {
          setAllLoaded(true);
        } else {
          uniquePosts.forEach((item) =>
            loadedIdentifiers.current.add(item.identifier)
          );

          const newPosts: PostData[] = uniquePosts.map((item) => ({
            identifier: item.identifier,
            username: item.owning_user || 'unknown',
            caption: item.caption || 'No caption provided',
            imageUrl: item.content,
            profilePic:
              item.owning_picture ||
              'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQdVgSoJ_6jouY4v5cmPt2mlTY7nS7gjMzng&s',
            comments: (item.comments || []).map((comment: any) => ({
              account: comment.account || 'unknown',
              content: comment.content || '',
              likes: comment.likes || [],
            })),
          }));

          setPosts((prevPosts) => [...prevPosts, ...newPosts]);
        }
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  };

  useEffect(() => {
    loadPosts();
  }, []);

  const feedRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const handleScroll = () => {
      const feedElement = feedRef.current;

      if (feedElement) {
        const isBottom =
          feedElement.scrollHeight === feedElement.scrollTop + feedElement.clientHeight;

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
      <div className="profile-page">

        <div className="profile-user-name">
            <h2 className="user-name">{user?.display_name || "No Username Found"}</h2>
        </div>

        <div className="profile-img">
            <img
                src={user?.profile_picture || "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQdVgSoJ_6jouY4v5cmPt2mlTY7nS7gjMzng&s"}
                alt={`Missing profile picture`}
                width="80"
                height="80"
                draggable="false"
            />
        </div>

        <div className="profile-name" dir="auto">
            <h3 className="name">{user?.name || "No Profile Name Found"}</h3>
        </div>

        <div className="profile-posts-box">
            {posts.map((post) => (
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
          {allLoaded && (
            <div className="end-of-feed">You have reached the end of the feed</div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ProfilePage;
