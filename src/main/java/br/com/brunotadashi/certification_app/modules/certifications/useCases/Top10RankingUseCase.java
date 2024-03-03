package br.com.brunotadashi.certification_app.modules.certifications.useCases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.brunotadashi.certification_app.modules.students.entities.CertificationStudentEntity;
import br.com.brunotadashi.certification_app.modules.students.repositories.CertificationStudentRepository;

@Service
public class Top10RankingUseCase {

  @Autowired
  private CertificationStudentRepository certificationStudentRepository;

  public List<CertificationStudentEntity> execute() {
    return this.certificationStudentRepository.findTop10ByOrderByGradeDesc();
  }
}
