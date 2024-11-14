import React, { useState, useEffect, useRef } from 'react';

const Upload = () => {
    const [caption, setCaption] = useState('');
    const [image, setImage] = useState(null);
    const [uploading, setUploading] = useState(false);
    const [uploadSuccess, setUploadSuccess] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const handleImageChange = (e) => {
        setImage(e.target.files[0]);
    }

    const convertFileToBase64 = (file) => {
        return new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = () => resolve(reader.result);
          reader.onerror = (error) => reject(error);
        });
      };

    const handleUpload = (e) => {
        e.preventDefault();

        if (!caption || !image) {
            setErrorMessage('Please fill in all fields and select an image.');
            return;
          }
      
          setUploading(true);
          setErrorMessage('');
          setUploadSuccess(false);

          convertFileToBase64(image)
      .then((base64Image) => {
        const postData = {
          content: base64Image,        // The Base64 encoded image
          caption: caption,
          likes: [],
          comments: [],
          owning_user: "Tamely" || "Anonymous",
          owning_picture: "NAN",       // Placeholder if no profile picture is needed
          identifier: "NAN"            // Placeholder; server generates unique ID
        };

        return fetch('http://127.0.0.1:7877/account/post', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(postData),
        });
      })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to upload post');
        }
        return response.json();
      })
      .then((data) => {
        setUploading(false);
        setUploadSuccess(true);
        setCaption('');
        setImage(null);
      })
      .catch((error) => {
        console.error('Error uploading post:', error);
        setErrorMessage('An error occurred. Please try again.');
        setUploading(false);
      });
    }
      
        return (
          <div className="upload-page" style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
            <h2>Upload a New Post</h2>
            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
            {uploadSuccess && <p style={{ color: 'green' }}>Post uploaded successfully!</p>}
            
            <form onSubmit={handleUpload} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>

              <label>
                Caption:
                <textarea
                  value={caption}
                  onChange={(e) => setCaption(e.target.value)}
                  required
                  style={{ padding: '8px', fontSize: '14px' }}
                />
              </label>
      
              <label>
                Image:
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleImageChange}
                  required
                />
              </label>
      
              <button type="submit" disabled={uploading} style={{ padding: '10px', fontSize: '16px', cursor: 'pointer' }}>
                {uploading ? 'Uploading...' : 'Upload Post'}
              </button>
            </form>
          </div>
        );
      };

export default Upload;