import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Homefeed from './pages/Homefeed.js';
import Explore from './pages/Explore.js';
import Nav from './components/Nav/Nav';
import './styles/Explore.css';
import './styles/App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Nav />
        <div className="content-container">
          <Routes>
            <Route path="/" element={<Navigate to="/home" />} />
            <Route path="/home" element={<Homefeed />} />
            <Route path="/explore" element={<Explore />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
