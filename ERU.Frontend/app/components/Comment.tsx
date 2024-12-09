import Like from "./Like";

interface Comment {
  account: string;
  content: string;
  likes: Like[];
}

export default Comment;