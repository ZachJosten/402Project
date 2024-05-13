package Backend.ICPC;

import Backend.ICPC.Controllers.ProblemController;
import Backend.ICPC.Models.*;
import Backend.ICPC.Models.WebsiteSpecific.Codeforces.CodeforcesProblem;
import Backend.ICPC.Models.WebsiteSpecific.uHunt.uHuntProblem;
import Backend.ICPC.Repositories.*;
import Backend.ICPC.Repositories.WebsiteSpecific.Codeforces.CFProblemRepository;
import Backend.ICPC.Repositories.WebsiteSpecific.uHunt.uHuntProblemRepository;
import org.hibernate.loader.custom.sql.SQLCustomQuery;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IcpcApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(IcpcApplication.class, args);
	}


/*
	@Bean
	CommandLineRunner initProblems(CFProblemRepository cfProblemRepository, uHuntProblemRepository uhuntProblemRepository, CoachRepository coachRepository, StudentRepository studentRepository, TeamRepository teamRepository)
	{
		return args -> {

			CodeforcesProblem cf1 = new CodeforcesProblem();
			cf1.setName("3N");
			cf1.setConId(1932);
			cf1.setInd("E");
			cfProblemRepository.save(cf1);

			uHuntProblem uh1 = new uHuntProblem();
			uh1.setName("Block");
			uh1.setUid(37);
			uh1.setNumP(101);
			uhuntProblemRepository.save(uh1);

			Coach coach1 = new Coach("John Doe", "johndoe@gmail.com", "12345");
			coachRepository.save(coach1);

			Coach coach2 = new Coach("Jane Doe", "janedoe@gmail.com", "54321");
			coachRepository.save(coach2);

			Student student1 = new Student("Jack", "jack@gmail.com", "123", 352);
			studentRepository.save(student1);

			Student student2 = new Student("Jill", "jill@gmail.com", "321", 621);
			studentRepository.save(student2);

			Student student3 = new Student("Adam", "adam@gmail.com", "678", 300);
			studentRepository.save(student3);

			Student student4 = new Student("Emma", "emma@gmail.com", "876", 3);
			studentRepository.save(student4);

			Team team1 = new Team(coach1);
			student1.setTeam(team1);
			student2.setTeam(team1);
			teamRepository.save(team1);
			studentRepository.save(student1);
			studentRepository.save(student2);

			Team team2 = new Team(coach2);
			student3.setTeam(team2);
			student4.setTeam(team2);
			teamRepository.save(team2);
			studentRepository.save(student3);
			studentRepository.save(student4);
		};



	}

 */


}
