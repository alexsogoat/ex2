package kr.ac.poly.ex2.repository;

import jakarta.transaction.Transactional;
import kr.ac.poly.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());

    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("=======================");

        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2() {
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("=======================");
        System.out.println(memo);
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("--------------");

        System.out.println("Total Pages: " + result.getTotalPages());
        System.out.println("Total Count: " + result.getTotalElements());
        System.out.println("Page Number: " + result.getNumber());
        System.out.println("Page Size: " + result.getSize());
        System.out.println("has next page?: " + result.hasNext());
        System.out.println("first page?: " + result.isFirst());
        System.out.println("--------------");
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(50L, 80L);
        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> System.out.println(memo));

//        for (Memo memo: result){
//            System.out.println(memo);
//        }
    }

    @Test
    @Commit
    @Transactional
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testGetListDesc() {
        List<Memo> list = memoRepository.getListDesc();
        for (Memo memo: list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testUpdateMemoText(){
        int updateCount = memoRepository.updateMemoText(30L, "mno가 30인 내용 수정");
    }

    @Test
    public void testUpdateMemoText2() {
        Memo memo = new Memo();
        memo.setMno(31);
        memo.setMemoText("31행 수정 Memo객체 참조값을 param으로 사용");
        int updateCount = memoRepository.updateMemoText2(memo);
    }

    @Test
    public void testGetListWithQuery(){
        Pageable pageable = PageRequest.of(0, 50, Sort.by("mno").ascending());
        Page<Memo> result = memoRepository.getListWithQuery(20L, pageable);
        result.get().forEach(
                memo -> System.out.println(memo)
        );
    }
}
