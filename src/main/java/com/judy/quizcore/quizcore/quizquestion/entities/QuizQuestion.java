package com.judy.quizcore.quizcore.quizquestion.entities;

import com.judy.quizcore.quizcore.common.entities.BaseEntity;
import com.judy.quizcore.quizcore.learninglog.entities.LearningLog;
import com.judy.quizcore.quizcore.learningsentence.dto.LearningSentenceEntityDto;
import com.judy.quizcore.quizcore.learningsentence.entities.LearningSentence;
import com.judy.quizcore.quizcore.quizsession.entities.QuizSession;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.judy.quizcore.quizcore.quizquestion.enums.QuestionType;

/**
 * 퀴즈 문제 엔티티
 * 퀴즈 세션에서 출제되는 개별 문제를 관리합니다.
 * 문제의 내용, 정답, 보기, 순서 등을 저장합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizQuestion extends BaseEntity {
    
    /**
     * 퀴즈 문제 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 식별자
     * 퀴즈 세션을 생성한 사용자를 구분합니다.
     * 최대 100자까지 저장 가능합니다.
     */
    @Column(length = 100)
    private Long userId;
    
    /**
     * 퀴즈 세션
     * 이 문제가 속한 퀴즈 세션입니다.
     * 다대일 관계로 연결됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private QuizSession quizSession;
    
    /**
     * 학습 문장
     * 이 문제의 기반이 되는 학습 문장입니다.
     * 다대일 관계로 연결됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private LearningSentence learningSentence;
    
    /**
     * 문제 순서
     * 퀴즈 세션 내에서 문제가 출제되는 순서입니다.
     * 1부터 시작하는 순차적인 번호입니다.
     */
    @Column(nullable = false)
    private Integer questionOrder;
    
    /**
     * 문제 유형
     * 문제의 출제 형태를 나타냅니다.
     * TRANSLATION(번역), COMPLETION(빈칸채우기), MULTIPLE_CHOICE(객관식) 등
     * enum으로 정의되어 타입 안정성을 보장합니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;
    
    /**
     * 문제 내용 (JSON 형태)
     * 문제 유형에 따라 다른 구조를 가집니다.
     * COMPLETION: ["The", "", "results", "", "our", "", "by", "15%."]
     * 최대 1000자까지 저장 가능합니다.
     */
    @Column(columnDefinition = "TEXT")
    private String question;
    
    /**
     * 정답 (JSON 형태)
     * 문제 유형에 따라 다른 구조를 가집니다.
     * COMPLETION: [{"index": 1, "answer": "quarterly"}, {"index": 3, "answer": "exceeded"}]
     * 최대 1000자까지 저장 가능합니다.
     */
    @Column(columnDefinition = "TEXT")
    private String correctAnswer;
    
    /**
     * 문제 보기
     * 객관식 문제의 경우 선택지를 JSON 형태로 저장합니다.
     * 예: ["선택지1", "선택지2", "선택지3", "선택지4"]
     * TEXT 타입으로 저장되어 JSON 데이터를 유연하게 처리할 수 있습니다.
     */
    @Column(columnDefinition = "TEXT")
    private String options;
    
    /**
     * 문제 해결 여부
     * 사용자가 이 문제를 풀었는지 여부를 나타냅니다.
     */
    @Column(nullable = false)
    private Boolean isSolved = false;
    
    /**
     * 학습 로그
     * 이 문제에 대한 사용자의 답변 기록입니다.
     * 일대일 관계로 연결됩니다.
     */
    @OneToOne(mappedBy = "quizQuestion", cascade = CascadeType.ALL)
    private LearningLog learningLog;

    /**
     * ID만으로 QuizQuestion 생성합니다.
     * 주로 JPA 관계 매핑에서 사용됩니다.
     *
     * @param id 퀴즈 질문 ID
     */
    public QuizQuestion(Long id) {
        this.id = id;
    }

    /**
     * LearningSentenceEntityDto를 기반으로 빈칸 뚫기 문제를 생성합니다.
     *
     * @param userId 사용자 ID
     * @param questionOrder 문제 순서
     * @param learningSentenceEntityDto 학습 문장 DTO
     * @return 생성된 퀴즈 문제
     */
    public static QuizQuestion of(Long userId, Long sessionId, Integer questionOrder, LearningSentenceEntityDto learningSentenceEntityDto) {
        QuizQuestion quizQuestion = new QuizQuestion();
        
        // 기본 설정
        quizQuestion.userId = userId;
        quizQuestion.quizSession = new QuizSession(sessionId);
        quizQuestion.questionOrder = questionOrder;
        quizQuestion.questionType = QuestionType.COMPLETION;
        quizQuestion.learningSentence = new LearningSentence(learningSentenceEntityDto.id());
        
        // 빈칸 뚫기 문제 생성
        quizQuestion.question = createCompletionQuestion(learningSentenceEntityDto.sentence());
        quizQuestion.correctAnswer = createCompletionAnswer(learningSentenceEntityDto.sentence());
        quizQuestion.options = createCompletionOptions(learningSentenceEntityDto.sentence());
        quizQuestion.isSolved = false;
        
        return quizQuestion;
    }
    
    /**
     * 빈칸 뚫기 문제를 위한 JSON 형태의 문제를 생성합니다.
     * 문장을 단어 단위로 분리하고 일부를 빈칸으로 만들어 배열로 저장합니다.
     */
    private static String createCompletionQuestion(String sentence) {
        String[] words = sentence.split("\\s+");
        if (words.length < 2) {
            return String.format("[\"%s\"]", sentence);
        }
        
        // 간단한 예시: 짝수 인덱스의 단어를 빈칸으로 만들기
        StringBuilder questionBuilder = new StringBuilder("[");
        for (int i = 0; i < words.length; i++) {
            if (i > 0) questionBuilder.append(", ");
            
            if (i % 2 == 1 && i < words.length - 1) { // 짝수 인덱스(1, 3, 5...)를 빈칸으로
                questionBuilder.append("\"\"");
            } else {
                questionBuilder.append("\"").append(words[i]).append("\"");
            }
        }
        questionBuilder.append("]");
        
        return questionBuilder.toString();
    }
    
    /**
     * 빈칸 뚫기 문제를 위한 JSON 형태의 정답을 생성합니다.
     * 빈칸의 인덱스와 정답을 객체 배열로 저장합니다.
     */
    private static String createCompletionAnswer(String sentence) {
        String[] words = sentence.split("\\s+");
        if (words.length < 2) {
            return String.format("[{\"index\": 0, \"answer\": \"%s\"}]", sentence);
        }
        
        // 빈칸이 있는 인덱스와 해당 정답을 찾기
        StringBuilder answerBuilder = new StringBuilder("[");
        boolean first = true;
        
        for (int i = 0; i < words.length; i++) {
            if (i % 2 == 1 && i < words.length - 1) { // 빈칸이 있는 인덱스
                if (!first) answerBuilder.append(", ");
                answerBuilder.append(String.format("{\"index\": %d, \"answer\": \"%s\"}", i, words[i]));
                first = false;
            }
        }
        
        answerBuilder.append("]");
        return answerBuilder.toString();
    }
    
    /**
     * 빈칸 뚫기 문제를 위한 선택지를 생성합니다.
     * 전체 문제에 대한 하나의 선택지 배열을 생성합니다.
     * 
     * @param sentence 원본 문장
     * @return 선택지 JSON 문자열
     */
    private static String createCompletionOptions(String sentence) {
        String[] words = sentence.split("\\s+");
        if (words.length < 2) {
            return "[]";
        }
        
        // 빈칸에 들어갈 수 있는 모든 단어들을 수집
        StringBuilder optionsBuilder = new StringBuilder("[");
        boolean first = true;
        
        for (int i = 0; i < words.length; i++) {
            if (i % 2 == 1 && i < words.length - 1) { // 짝수 인덱스(1, 3, 5...)가 빈칸
                if (!first) optionsBuilder.append(", ");
                
                String correctAnswer = words[i];
                optionsBuilder.append("\"").append(correctAnswer).append("\"");
                
                first = false;
            }
        }
        
        optionsBuilder.append("]");
        return optionsBuilder.toString();
    }
    
    /**
     * 문제를 해결된 상태로 표시합니다.
     */
    public void markAsSolved() {
        this.isSolved = true;
    }
}
