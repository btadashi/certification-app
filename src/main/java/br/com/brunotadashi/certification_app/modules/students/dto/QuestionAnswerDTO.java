package br.com.brunotadashi.certification_app.modules.students.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionAnswerDTO {

  private UUID questionID;
  private UUID alternativeID;
  private boolean isCorrect;

}
