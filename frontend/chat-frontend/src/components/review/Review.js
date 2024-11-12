import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import axios from '../axios/AxiosInstance';
import './Review.css';

const Review = ({ tutorId }) => {
    const [reviews, setReviews] = useState([]);
    const [newReview, setNewReview] = useState({ rating: 0, reviewText: '' });
    const [editingReview, setEditingReview] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    // 로그인된 사용자 ID 추출
    const token = localStorage.getItem('jwtToken');
    const userId = token ? jwtDecode(token).userId : null;

    // 리뷰 표시 상태를 확인하여 fetch 호출
    useEffect(() => {
        if (tutorId) {
            fetchReviews();
        }
    }, [tutorId, currentPage]);

    const fetchReviews = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/review`, {
                params: { tutorId, page: currentPage, size: 5 },
            });
            setReviews(response.data.content || []); // 기본값을 빈 배열로 설정
            setTotalPages(response.data.totalPages || 0); // totalPages도 기본값 설정
        } catch (error) {
            console.error('Failed to fetch reviews:', error);
            setReviews([]); // 에러 발생 시 기본값 설정
        }
    };

    const handleReviewSubmit = async () => {
        try {
            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/review`, {
                tutorProfileId: tutorId,
                ...newReview,
            });
            alert('리뷰가 작성되었습니다!');
            setNewReview({ rating: 0, reviewText: '' });
            fetchReviews();
        } catch (error) {
            console.error('Failed to submit review:', error);
        }
    };

    const handleReviewEdit = async () => {
        try {
            await axios.put(`${process.env.REACT_APP_BACKEND_URL}/review/${editingReview.reviewId}`, editingReview);
            alert('리뷰가 수정되었습니다!');
            setEditingReview(null);
            fetchReviews();
        } catch (error) {
            console.error('Failed to edit review:', error);
        }
    };

    const handleReviewDelete = async (reviewId) => {
        try {
            await axios.delete(`${process.env.REACT_APP_BACKEND_URL}/review/${reviewId}`);
            alert('리뷰가 삭제되었습니다!');
            fetchReviews();
        } catch (error) {
            console.error('Failed to delete review:', error);
        }
    };

    const handleRatingChange = (value, isEditing) => {
        if (isEditing) {
            setEditingReview((prev) => ({ ...prev, rating: value }));
        } else {
            setNewReview((prev) => ({ ...prev, rating: value }));
        }
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setCurrentPage(newPage);
        }
    };

    const renderStars = (rating, isEditing) => {
        const handleStarClick = (value) => {
            if (isEditing) {
                setEditingReview((prev) => ({ ...prev, rating: value }));
            } else {
                setNewReview((prev) => ({ ...prev, rating: value }));
            }
        };

        return (
            <div className="stars">
                {Array.from({ length: 5 }, (_, index) => (
                    <div key={index} className="star-wrapper">
                        {/* Half star */}
                        <span
                            className={`star half ${rating >= index + 0.5 ? 'filled' : ''}`}
                            onClick={() => handleStarClick(index + 0.5)}
                        >
                            ★
                        </span>
                        {/* Full star */}
                        <span
                            className={`star full ${rating >= index + 1 ? 'filled' : ''}`}
                            onClick={() => handleStarClick(index + 1)}
                        >
                            ★
                        </span>
                    </div>
                ))}
            </div>
        );
    };


    return (
        <div className="review-container">
        <h3>리뷰</h3>
        {reviews.length > 0 ? (
            <div className="review-list">
                {reviews.map((review) => (
                    <div className="review-item">
                    <div className="review-header">
                        <p className="review-writer"><strong>{review.writerNickname}</strong></p>
                        <div className="review-rating-stars">
                            {renderStars(review.rating, false)}
                        </div>
                    </div>
                    <p className="review-text">{review.reviewText}</p>
                    <p className="review-date">{new Date(review.createdAt).toLocaleString()}</p>
                    {/* 수정/삭제 버튼은 작성자만 볼 수 있도록 */}
                    {userId === review.writerId && (
                        <div className="review-actions">
                            <button onClick={() => setEditingReview(review)}>수정</button>
                            <button onClick={() => handleReviewDelete(review.id)}>삭제</button>
                        </div>
                    )}
                </div>
                ))}
            </div>
        ) : (
            <p>리뷰가 없습니다.</p>
        )}

            {/* 페이지네이션 */}
            <div className="pagination">
                <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0}>
                    &lt;
                </button>
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index}
                        onClick={() => handlePageChange(index)}
                        className={index === currentPage ? 'active' : ''}
                    >
                        {index + 1}
                    </button>
                ))}
                <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
                    &gt;
                </button>
            </div>

            {/* 리뷰 작성 */}
            <h3>리뷰 작성</h3>
            <div className="review-form">
                <div className="stars-container">
                    {renderStars(newReview.rating, false)}
                </div>
                <textarea
                    value={newReview.reviewText}
                    onChange={(e) => setNewReview({ ...newReview, reviewText: e.target.value })}
                    placeholder="리뷰 내용을 작성하세요"
                ></textarea>
                <button className="floating-button"onClick={handleReviewSubmit}>리뷰 작성</button>
            </div>

            {/* 리뷰 수정 */}
            {editingReview && (
                <div className="review-form">
                    <h3>리뷰 수정</h3>
                    {renderStars(editingReview.rating, true)}
                    <textarea
                        value={editingReview.reviewText}
                        onChange={(e) =>
                            setEditingReview((prev) => ({ ...prev, reviewText: e.target.value }))
                        }
                        placeholder="리뷰 내용을 수정하세요"
                    ></textarea>
                    <button onClick={handleReviewEdit}>리뷰 수정</button>
                    <button onClick={() => setEditingReview(null)}>취소</button>
                </div>
            )}
        </div>
    );
};

export default Review;
