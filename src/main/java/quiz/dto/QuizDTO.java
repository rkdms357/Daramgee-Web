package quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizDTO {
    private int quizId;
    private String question;
    private String answer;
    // DB의 CHOICE 컬럼을 잘라서 여기에 넣음
    private String[] options; // 보기 배열 (매수/공매도/손절매...)
}