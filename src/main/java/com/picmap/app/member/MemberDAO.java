package com.picmap.app.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.picmap.app.board.BoardDTO;
import com.picmap.app.follow.FollowDTO;
import com.picmap.app.travel.TravelDTO;




@Repository
public class MemberDAO {

	@Autowired
private SqlSession sqlSession;
private final String NAMESPACE="com.picmap.app.member.MemberDAO.";


public int idCheck(MemberDTO memberDTO) throws Exception {
	return sqlSession.selectOne(NAMESPACE + "idCheck", memberDTO);
}
public int phoneCheck(MemberDTO memberDTO) throws Exception {
	return sqlSession.selectOne(NAMESPACE + "phoneCheck", memberDTO);
}
public int memberNickName(MemberDTO memberDTO) throws Exception {
	return sqlSession.selectOne(NAMESPACE + "memberNickName", memberDTO);
}
	//로그인 
	public MemberDTO login(MemberDTO memberDTO) throws Exception{
		return sqlSession.selectOne(NAMESPACE+"login",memberDTO);
	}	
	//회원가입
	public int join(MemberDTO memberDTO) throws Exception {
		return sqlSession.insert(NAMESPACE + "join", memberDTO);
	}

//마이페이지
	public MemberDTO mypage(MemberDTO memberDTO) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "mypage", memberDTO);
	}
	public MemberDTO detail(MemberDTO memberDTO) throws Exception {
		return sqlSession.selectOne(NAMESPACE + "detail", memberDTO);
	}	
	public int update(MemberDTO memberDTO) throws Exception {
		return sqlSession.update(NAMESPACE + "update", memberDTO);
	}

	public int delete(MemberDTO memberDTO) throws Exception {
		return sqlSession.update(NAMESPACE + "delete", memberDTO);
	}
	
	public int follow(FollowDTO followDTO)throws Exception {
		return sqlSession.insert(NAMESPACE + "follow", followDTO);
	}	
	public Long countFromFollow(MemberDTO memberDTO)throws Exception {
		return sqlSession.selectOne(NAMESPACE + "countFromFollow", memberDTO);
	}	
	public Long countToFollow(MemberDTO memberDTO)throws Exception {
		return sqlSession.selectOne(NAMESPACE + "countToFollow", memberDTO);
	}	
	public Integer followCheck(FollowDTO followDTO)throws Exception {
		return sqlSession.selectOne(NAMESPACE + "followCheck", followDTO);
	}	
	public int followDelete(FollowDTO followDTO) throws Exception {
		return sqlSession.delete(NAMESPACE + "followDelete", followDTO);
	}
	public List<MemberDTO> fromFollowList(Map<String, Object> map)throws Exception {
		return sqlSession.selectList(NAMESPACE + "fromFollowList", map);
	}	
	public List<MemberDTO> toFollowList(Map<String, Object> map)throws Exception {
		return sqlSession.selectList(NAMESPACE + "toFollowList", map);
	}	
	//게시판(게시글 리스트)
	public List<TravelDTO> getList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList(NAMESPACE+"getList",map);
	}
	public int kakaoDelete(MemberDTO memberDTO) throws Exception {
		return sqlSession.delete(NAMESPACE + "kakaoDelete", memberDTO);
	}
	public Long getPostCountByMember(MemberDTO memberDTO)throws Exception {
		return sqlSession.selectOne(NAMESPACE + "getPostCountByMember", memberDTO);
	}	
	public Long getSavePostCountByMember(MemberDTO memberDTO)throws Exception {
		return sqlSession.selectOne(NAMESPACE + "getSavePostCountByMember", memberDTO);
	}	

    // 아이디 찾기
    public MemberDTO findID(MemberDTO memberDTO) throws Exception {
        return sqlSession.selectOne(NAMESPACE + "findID", memberDTO);
    }

    public MemberDTO findPassword(MemberDTO memberDTO) throws Exception {
        return sqlSession.selectOne(NAMESPACE + "findPassword", memberDTO);
    }
    
	public int proFileUpdate(MemberDTO memberDTO) throws Exception {
		return sqlSession.update(NAMESPACE + "proFileUpdate", memberDTO);
	}
	
	public Integer memberFollowDelete(MemberDTO memberDTO) throws Exception {
		return sqlSession.delete(NAMESPACE + "memberFollowDelete", memberDTO);
	}
}
