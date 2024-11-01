import React, { useEffect, useState } from 'react';
import axios from '../axios/AxiosInstance';
import { useNavigate } from 'react-router-dom';
import './MyPosts.css';

const MyPosts = () => {
    const [posts, setPosts] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 10;
    const navigate = useNavigate();

    useEffect(() => {
        fetchMyPosts();
    }, [currentPage]);

    const fetchMyPosts = async () => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_BACKEND_URL}/tutee/postings/me`,
                {
                    params: { page: currentPage, size: pageSize },
                }
            );
            setPosts(response.data.content);
        } catch (error) {
            console.error('Failed to fetch my posts:', error);
        }
    };

    const handleEdit = (postingId) => {
        navigate(`/my-profile/edit-post/${postingId}`);
    };
    const handleDelete = async (postingId) => {
        if (window.confirm('정말로 삭제하시겠습니까?')) {
            try {
                await axios.delete(
                    `${process.env.REACT_APP_BACKEND_URL}/tutee/postings/${postingId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem('token')}`,
                        },
                    }
                );
                alert('게시물이 삭제되었습니다.');
                fetchMyPosts(); // 게시물 삭제 후 목록을 갱신
            } catch (error) {
                console.error('Failed to delete post:', error);
                alert('게시물 삭제에 실패했습니다.');
            }
        }
    };

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    return (
        <div className="my-posts-container">
            <h2>내가 작성한 글 목록</h2>
            <div className="post-grid">
                {posts.map(post => (
                    <div key={post.postingId} className="post-item">
                        <h3 className="post-title">{post.studentGrade} - {post.studentSchool}</h3>
                        <p className="post-detail">성격: {post.personality}</p>
                        <p className="post-detail">수업 유형: {post.tutoringType}</p>
                        <p className="post-detail">가능한 시간: {post.possibleSchedule}</p>
                        <p className="post-detail">수업 수준: {post.level}</p>
                        <p className="post-detail">수업료: {post.fee}만원</p>
                        <button onClick={() => handleEdit(post.postingId)} className="edit-button">수정</button>
                        <button onClick={() => handleDelete(post.postingId)} className="delete-button">삭제</button>
                    </div>
                ))}
            </div>

            {/* 페이지네이션 */}
            <div className="pagination">
                {Array.from({ length: Math.ceil(posts.length / pageSize) }, (_, index) => (
                    <button
                        key={index}
                        onClick={() => handlePageChange(index)}
                        className={`page-button ${index === currentPage ? 'active' : ''}`}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>
    );
};

export default MyPosts;
