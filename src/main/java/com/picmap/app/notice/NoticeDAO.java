package com.picmap.app.notice;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.picmap.app.board.BoardDAO;
import com.picmap.app.board.BoardDTO;
import com.picmap.app.board.BoardFileDTO;
import com.picmap.app.travel.TravelDTO;
import com.picmap.app.util.Pager;

@Repository
public class NoticeDAO implements BoardDAO{
	



	@Autowired
	private SqlSession sqlSession;
	
	private final String NAMESPACE = "com.picmap.app.notice.NoticeDAO.";
	

	
	//게시판(게시글 리스트)
	@Override
	public List<BoardDTO> getList(Pager pager) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList(NAMESPACE + "getList", pager);
	}
	
	@Override
	public Long getTotalCount(Pager pager) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(NAMESPACE + "getTotalCount", pager);
	}


	//게시글 작성
	@Override
	public int add(BoardDTO boardDTO) throws Exception {
		return sqlSession.insert(NAMESPACE+"add", boardDTO);
	}

	@Override
	public int addFile(BoardFileDTO boardFileDTO) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int update(BoardDTO boardDTO) throws Exception {
		return sqlSession.update(NAMESPACE+"update", boardDTO);
	}
	
	public NoticeDTO getDetail(NoticeDTO noticeDTO) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "getDetail", noticeDTO);
	}
	
	
	public Integer delete(NoticeDTO noticeDTO) throws Exception {
		sqlSession.delete(NAMESPACE + "deleteHeart", noticeDTO);
		return sqlSession.delete(NAMESPACE + "delete", 	noticeDTO);
	}
	
	//조회수
	public int hit(NoticeDTO noticeDTO) throws Exception {
		return sqlSession.update(NAMESPACE+"hit", noticeDTO);
	}
	
}
