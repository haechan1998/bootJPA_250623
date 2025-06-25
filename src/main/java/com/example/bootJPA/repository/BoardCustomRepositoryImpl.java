package com.example.bootJPA.repository;

import com.example.bootJPA.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.bootJPA.entity.QBoard.board;

@Slf4j
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public BoardCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Board> searchBoard(String type, String keyword, Pageable pageable) {
        /* BooleanExpression : 일반적으로 동적쿼리를 작성 할 때 사용하는 객체 */
        /*
        * select * from board
        * where isDel = 'N' and title like '%keyword%';
        * where isDel = 'N' and (title like '%keyword%' or writer like '%keyword%');
        *
        * BooleanExpression condition = board.isDel.eq('N')
        * condition = condition.and(board.title.containsIgnoreCase(keyword)
        *
        * */
        BooleanExpression condition = null;

        // 동적 검색 조건 추가
        if(type != null && keyword != null){
            // Type 이 여러개 들어 올 경우 배열로 처리
            String[] typeArr = type.split("");
            for(String t : typeArr){
                switch (t){
                    case "t": condition = (condition == null) ?
                            board.title.containsIgnoreCase(keyword) :
                            condition.or(board.title.containsIgnoreCase(keyword));
                        break;

                    case "w": condition = (condition == null) ?
                            board.writer.containsIgnoreCase(keyword) :
                            condition.or(board.writer.containsIgnoreCase(keyword));
                        break;

                    case "c": condition = (condition == null) ?
                            board.content.containsIgnoreCase(keyword) :
                            condition.or(board.content.containsIgnoreCase(keyword));
                        break;

                    default: break;

                }
            }
        }
        // 쿼리 작성 및 페이징 적용
        List<Board> result = jpaQueryFactory
                .selectFrom(board) // selectFrom => select *
                .where(condition)
                .orderBy(board.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 검색된 데이터의 전체 개수 조회
        long total = jpaQueryFactory
                .selectFrom(board)
                .where(condition)
                .fetch().size();

        // PageImpl 을 구성하는건 List<>(페이징 되는 엔티티), pageable, total(데이터 개수)
        return new PageImpl<>(result, pageable, total);

    }

}
