
import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

const Home = () => {
    return (
        <div className="home-container">
            <div className="categories">
                <Link to="/posts" className="category-link">학생 찾기</Link>
                <Link to="/tutor-profiles" className="category-link">선생님 찾기</Link>
            </div>
        </div>
    );
};

export default Home;
