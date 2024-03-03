package br.com.brunotadashi.certification_app.modules.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brunotadashi.certification_app.modules.students.dto.StudentCertificationAnswerDTO;
import br.com.brunotadashi.certification_app.modules.students.dto.VerifyHasCertificationDTO;
import br.com.brunotadashi.certification_app.modules.students.useCases.StudentCertificationAnswersUseCase;
import br.com.brunotadashi.certification_app.modules.students.useCases.VerifyIfHasCertificationUseCase;

@RestController
@RequestMapping("/students")
public class StudentController {

  @Autowired
  private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

  @Autowired
  private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

  @PostMapping("/verifyIfHasCertification")
  public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO) {

    var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);

    if (result) {
      return "Usuário já vez a prova";
    }

    return "Usuário pode fazer a prova";
  }

  @PostMapping("/certification/answer")
  public ResponseEntity<Object> certificationAnswer(
      @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) throws Exception {
    try {
      var result = this.studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
