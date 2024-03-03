package br.com.brunotadashi.certification_app.modules.students.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.brunotadashi.certification_app.modules.questions.entities.QuestionEntity;
import br.com.brunotadashi.certification_app.modules.questions.repositories.QuestionRepository;
import br.com.brunotadashi.certification_app.modules.students.dto.StudentCertificationAnswerDTO;
import br.com.brunotadashi.certification_app.modules.students.dto.VerifyHasCertificationDTO;
import br.com.brunotadashi.certification_app.modules.students.entities.AnswersCertificationsEntity;
import br.com.brunotadashi.certification_app.modules.students.entities.CertificationStudentEntity;
import br.com.brunotadashi.certification_app.modules.students.entities.StudentEntity;
import br.com.brunotadashi.certification_app.modules.students.repositories.CertificationStudentRepository;
import br.com.brunotadashi.certification_app.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswersUseCase {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private CertificationStudentRepository certificationStudentRepository;

  @Autowired
  private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

  public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

    var hasCertification = this.verifyIfHasCertificationUseCase
        .execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

    if (hasCertification) {
      throw new Exception("Você já tirou sua certificação!");
    }

    List<QuestionEntity> questionsEntity = this.questionRepository.findByTechnology(dto.getTechnology());
    List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

    AtomicInteger correctAnswers = new AtomicInteger(0);

    dto.getQuestionsAnswers().stream().forEach(questionAnswer -> {
      var question = questionsEntity.stream().filter(q -> q.getId().equals(questionAnswer.getQuestionID()))
          .findFirst().get();

      var findCorrectAlternative = question.getAlternatives().stream()
          .filter(alternative -> alternative.isCorrect()).findFirst().get();

      if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
        questionAnswer.setCorrect(true);
        correctAnswers.incrementAndGet();
      } else {
        questionAnswer.setCorrect(false);
      }

      var answersCertificationsEntity = AnswersCertificationsEntity.builder()
          .answerID(questionAnswer.getAlternativeID())
          .questionID(questionAnswer.getAlternativeID())
          .isCorrect(questionAnswer.isCorrect())
          .build();

      answersCertifications.add(answersCertificationsEntity);

    });

    var student = this.studentRepository.findByEmail(dto.getEmail());
    UUID studentID;

    if (student.isEmpty()) {
      var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
      studentCreated = this.studentRepository.save(studentCreated);
      studentID = studentCreated.getId();
    } else {
      studentID = student.get().getId();
    }

    CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
        .technology(dto.getTechnology())
        .studentID(studentID)
        .grade(correctAnswers.get())
        .build();

    var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

    answersCertifications.forEach(answerCertification -> {
      answerCertification.setCertificationID(certificationStudentEntity.getId());
      answerCertification.setCertificationStudentEntity(certificationStudentEntity);
    });

    certificationStudentEntity.setAnswersCertificationsEntity(answersCertifications);

    certificationStudentRepository.save(certificationStudentEntity);

    return certificationStudentCreated;
  }
}
