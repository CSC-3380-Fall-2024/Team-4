import React, { useState, useEffect, useRef } from 'react';
import '../styles/Explore.css';

// Define the Post interface
interface Post {
  id: number;
  imageUrl: string;
  username: string;
  caption: string;
}

const Explore: React.FC = () => {
  const initialPosts: Post[] = [
    { id: 1, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player 1', caption: 'Caption 1' },
    { id: 2, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player 2', caption: 'Caption 2' },
    { id: 3, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player 3', caption: 'Caption 3' },
    { id: 4, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player 4', caption: 'Caption 4' },
    { id: 5, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player 5', caption: 'Caption 5' },
    { id: 6, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player 6', caption: 'Caption 6' },
    { id: 7, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player 7', caption: 'Caption 7' },
  ];

  const [posts, setPosts] = useState<Post[]>(initialPosts);
  const [loading, setLoading] = useState<boolean>(false);
  const [enlargedPost, setEnlargedPost] = useState<Post | null>(null);

  // Load more posts
  const loadMorePosts = () => {
    if (loading) return;
    setLoading(true);

    setTimeout(() => {
      const newPosts: Post[] = [
        { id: posts.length + 1, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player ' + (posts.length + 1), caption: 'New Caption ' + (posts.length + 1) },
        { id: posts.length + 2, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player ' + (posts.length + 2), caption: 'New Caption ' + (posts.length + 2) },
        { id: posts.length + 3, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player ' + (posts.length + 3), caption: 'New Caption ' + (posts.length + 3) },
        { id: posts.length + 4, imageUrl: 'https://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/1966.png', username: 'Player ' + (posts.length + 4), caption: 'New Caption ' + (posts.length + 4) },
      ];
      setPosts((prevPosts) => [...prevPosts, ...newPosts]);
      setLoading(false);
    }, 1000); // Simulate network delay
  };

  // Handle clicking on a post to enlarge
  const handlePostClick = (post: Post) => {
    setEnlargedPost(post);
  };

  // Handle closing the enlarged post view
  const closeEnlargedPost = () => {
    setEnlargedPost(null);
  };

  // Infinite scroll using IntersectionObserver
  const bottomRef = useRef<HTMLDivElement | null>(null);
  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting && !loading) {
          loadMorePosts();
        }
      },
      { threshold: 1.0 }
    );

    if (bottomRef.current) {
      observer.observe(bottomRef.current);
    }

    return () => {
      if (bottomRef.current) {
        observer.unobserve(bottomRef.current);
      }
    };
  }, [loading]);

  return (
    <div className="explore-container">
      <div className="explore-grid">
        {posts.map((post) => (
          <div key={post.id} className="explore-item" onClick={() => handlePostClick(post)}>
            <img src={post.imageUrl} alt="Explore post" />
          </div>
        ))}
      </div>

      {loading && <div className="loading">Loading more posts...</div>}

      {/* Enlarged Post View */}
      {enlargedPost && (
        <div className="post-modal" onClick={closeEnlargedPost}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <img src={enlargedPost.imageUrl} alt="Enlarged post" />
            <div className="modal-details">
              <h3>{enlargedPost.username}</h3>
              <p>{enlargedPost.caption}</p>
              <div className="post-actions">
                <button>Comment</button>
                <button>Save</button>
              </div>
            </div>
            <div className="close-btn" onClick={closeEnlargedPost}>X</div>
          </div>
        </div>
      )}

      {/* Bottom ref element for IntersectionObserver */}
      <div ref={bottomRef}></div>
    </div>
  );
};

export default Explore;
