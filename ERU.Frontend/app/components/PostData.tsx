import Comment from './Comment';

interface PostData {
  identifier: string;
  username: string;
  caption: string;
  imageUrl: string;
  profilePic: string;
  comments: Comment[];
}

export default PostData;