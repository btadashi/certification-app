package br.com.brunotadashi.certification_app.modules.students.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentCertificationAnswerDTO {

  private String email;
  private String technology;
  private List<QuestionAnswerDTO> questionsAnswers;
}
