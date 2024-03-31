package io.lecture.domain.lecture;

import io.lecture.domain.lecture.domain.NewLecture;
import io.lecture.domain.lecture.domain.NewLectureRegs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
public class LectureServiceConCurrencyTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureRegsRepository lectureRegsRepository;

    @Autowired
    LectureService lectureService;

    @Test
    @DisplayName("강의신청 동시성 30명 테스트")
    void con_currency_apply() throws InterruptedException {
        // given
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        NewLecture lecture = new NewLecture("김준우", "1강연장", 10, startAt, "스프링 강연");
        Long lectureId = lectureRepository.append(lecture);

        // when
        int numberOfThreads = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    lectureService.apply("강연신청",
                            new NewLectureRegs(ThreadLocalRandom.current().nextInt(10000, 100000), lectureId));
                }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        int count = lectureRegsRepository.countByLectureId(lectureId);
        assertThat(count).isEqualTo(10);
    }

}

