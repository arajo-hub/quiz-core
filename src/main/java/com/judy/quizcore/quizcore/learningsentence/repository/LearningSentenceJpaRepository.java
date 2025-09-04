package com.judy.quizcore.quizcore.learningsentence.repository;

import com.judy.quizcore.quizcore.learningsentence.entities.LearningSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningSentenceJpaRepository extends JpaRepository<LearningSentence, Long> {
    
    /**
     * 랜덤으로 학습문장 하나를 가져옵니다.
     * @return 랜덤 학습문장 (Optional)
     */
    @Query(value = "SELECT * FROM learning_sentence ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<LearningSentence> findRandomLearningSentence();
}
