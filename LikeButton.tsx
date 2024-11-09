/* Tutorial referenced: https://www.youtube.com/watch?v=BZluxXqVnQY */

/* This line imports React and the useState function from the React library
* useState is a hook that allows for the creation and management of state variables in functional components
* State is like a variable that, when updated, automatically causes the component to re-render (update what the user sees)*/
import React, { useState, FC} from "react";

/* A component is a reusable piece of code that can contain its own logic and UI
* In this case, LikeButton manages a "like" count and displays it */
const LikeButton: FC = function () {

    /* "likes" is the current number of likes, initially set to 0
    * setLikes is a function that updates likes
    * Whenever setLikes is called, the component re-renders with the new "likes" value */
    const [likes, setLikes] = useState<number>(0);

    /* toggleLike checks if "likes" is greater than 0
    * If true, it means the item is already liked
    * So, when the like button is clicked a second time, it will remove a like (un-like)
    * If false, then the item isn't liked yet
    * So, when the like button is clicked, the like count will be incremented by 1
    * Function is void because it doesn't return a value */
    function toggleLike(): void {
        if (likes > 0) {
            setLikes(likes - 1);
        }
        else {
            setLikes(likes + 1);
        }
    }

    /* getLikeText ensures correct grammar
    * if the like count is 1, show "like"
    * if the like count is 0 or greater than 1, show "likes" */
    function getLikeText(): string {
        if (likes === 1) {
            return "likes";
        }
        else {
            return "like";
        }
    }

    /* "return" specifies what the user will see */
    return (
        <div>
            {/* There is a button called "like", when clicked the button will call the function toggleLike */}
            <button onClick = {toggleLike}> like </button>
            {/* <p> tag is used to display the current number of likes, followed by the world "like" or "likes" */}
            <p>
                {likes} {getLikeText()}
            </p>
        </div>
    );
}

/* Export the LikeButton component so that it can be imported and used in other parts of the application */
export default LikeButton;