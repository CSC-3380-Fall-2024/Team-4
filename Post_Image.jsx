//tutorial:
//https://dev.to/shieldstring/upload-images-videos-and-audio-in-react-js-using-cloudinary-3ji
//discussion about creating fetch requests to a cloud
//https://community.cloudinary.com/discussion/116/is-it-safe-to-have-cloud-name-and-preset-in-front-end-code-upload-question
//version1

import React, { useState } from "react";

function Input() {
  const [image, setImage] = useState("");

  const uploadImage = (files) => {
    const formData = new FormData();

    formData.append("file", files[0]);
    formData.append("upload_preset", "<your upload preset>");
    fetch(
      "https://api.cloudinary.com/v1_1/<your cloud name>/image/upload", //ask about our cloud name
      {
        method: "POST",
        body: formData,
      }
    )
      .then((response) => response.json())
      .then((data) => {
        setImage(data.secure_url);
      });
  };
  return <div>
    <input type="file onChange={(e) => uploadImage(e.target.files)}"></input>
    <img
        src={image}
    />
  </div>;
}
export default input;