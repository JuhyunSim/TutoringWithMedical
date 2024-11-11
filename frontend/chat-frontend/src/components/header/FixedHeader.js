import React from 'react';
import { Link } from 'react-router-dom';
import './FixedHeader.css';

const FixedHeader = ({ isLoggedIn, onLogout }) => {
    return (
        <header className="fixed-header">
            <div className="auth-buttons">
                {isLoggedIn ? (
                    <>
                        <Link to="/me" className="auth-link">내 정보</Link>
                        <button onClick={onLogout} className="auth-link">로그아웃</button>
                    </>
                ) : (
                    <>
                        <Link to="/login" className="auth-link">로그인</Link>
                        <Link to="/signup" className="auth-link">회원가입</Link>
                    </>
                )}
            </div>
            
            <Link to="/" className="linkStyle">
                <h1>의대생과 과외하기</h1>
            </Link>
            
        </header>
    );
};

export default FixedHeader;
