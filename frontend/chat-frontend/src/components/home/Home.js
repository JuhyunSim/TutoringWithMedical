
import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

const Home = () => {
    return (
        <div className="main-content">
            {/* Categories Section */}
            <div className="categories">
                <Link to="/posts" className="category-link">학생 찾기</Link>
                <Link to="/find-teacher" className="category-link">선생님 찾기</Link>
            </div>
        </div>
    );
};

export default Home;
