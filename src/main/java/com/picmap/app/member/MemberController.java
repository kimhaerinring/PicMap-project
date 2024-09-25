package com.picmap.app.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.picmap.app.board.BoardDTO;
import com.picmap.app.follow.FollowDTO;
import com.picmap.app.kakaomember.KakaoMemberDAO;
import com.picmap.app.kakaomember.KakaoMemberDTO;
import com.picmap.app.travel.TravelDTO;
import com.picmap.app.util.Scroller;


@Controller
@RequestMapping("/member/*")
public class MemberController {
	@Autowired
	private MemberService memberService;

	@GetMapping("memberNickName")
	public String memberNickName(MemberDTO memberDTO, Model model) throws Exception {
		int result = memberService.memberNickName(memberDTO);
		model.addAttribute("msg", result);
		return "/commons/result";
	}

	@GetMapping("idCheck")
	public String idCheck(MemberDTO memberDTO, Model model) throws Exception {
		int result = memberService.idCheck(memberDTO);
		model.addAttribute("msg", result);
		return "/commons/result";
	}

	@GetMapping("phoneCheck") 
	public String phoneCheck(MemberDTO memberDTO, Model model) throws Exception {
		int result = memberService.phoneCheck(memberDTO);
		model.addAttribute("msg", result);
		return "/commons/result";
	}
	
	
//로그인
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public void login(Model model, @CookieValue(name = "remember ", required = false, defaultValue = "") String value)
			throws Exception {

		model.addAttribute("id", value);
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(Model model, MemberDTO memberDTO, String remember, HttpServletResponse response, String prevPage,
			HttpSession session) throws Exception {
		if (remember != null) {
			Cookie cookie = new Cookie("remember", memberDTO.getMemberId());
			cookie.setMaxAge(60 * 60);
			response.addCookie(cookie);
		} else {
			Cookie cookie = new Cookie("remember", "");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		memberDTO = memberService.login(memberDTO, session);
		String url = "/commons/message";

		if (memberDTO != null) {

			model.addAttribute("result", "로그인성공");
			model.addAttribute("url", prevPage);

		} else {
			model.addAttribute("result", "아이디 혹은 패스워드 확인 바랍니다");
			model.addAttribute("url", prevPage);
		}

		return url;

	}

	// 회원가입

	@RequestMapping(value = "join", method = RequestMethod.GET)
	public void join() {
	}

	@RequestMapping(value = "join", method = RequestMethod.POST)
	public String join(MemberDTO memberDTO, MultipartFile files, HttpSession session,Model model) throws Exception {

		int result = memberService.join(memberDTO, files, session);
		String url = "/commons/message";
		if (result > 0) {
			model.addAttribute("result", "회원가입 성공");
			model.addAttribute("url", "/");
		}
		return url;
	}

	// 로그아웃
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		memberService.logout(session);

		return "redirect:/";
	}
	
	@PostMapping("list")
	@ResponseBody
	public List<TravelDTO> getList(MemberDTO memberDTO, Scroller scroller) throws Exception {
		return memberService.getList(memberDTO, scroller);
	}
//마이페이지
	@RequestMapping(value = "mypage", method = RequestMethod.GET)
	public String mypage(MemberDTO memberDTO, Model model, HttpSession session) throws Exception {
		memberDTO = memberService.detail(memberDTO);
		if(memberDTO.getMemberEmail() == null) {
			String url = "/commons/message";

				model.addAttribute("result", "탈퇴한 회원입니다.");
				model.addAttribute("url", "/");
			return  url;
		}
		 MemberDTO dto = (MemberDTO) session.getAttribute("member");


	        // 회원의 게시글 수 가져오기
	        Long postCount = memberService.getPostCountByMember(memberDTO);
	        // 게시글 수를 모델에 담기
	        model.addAttribute("postCount", postCount);
	        // 회원의 게시글 수 가져오기
	        Long savePostCount = memberService.getSavePostCountByMember(memberDTO);
	        // 게시글 수를 모델에 담기
	        model.addAttribute("savePostCount", savePostCount);
		Long following = memberService.countFromFollow(memberDTO);
		Long follower = memberService.countToFollow(memberDTO);
		model.addAttribute("member", memberDTO);
		model.addAttribute("follower", follower);
		model.addAttribute("following", following);
		MemberDTO myDTO = (MemberDTO) session.getAttribute("member");

		if (myDTO != null) {
			FollowDTO followDTO = new FollowDTO();
			followDTO.setFromFollow(myDTO.getMemberNum());
			followDTO.setToFollow(memberDTO.getMemberNum());
			int followCheck = memberService.followCheck(followDTO);
			System.out.println(followCheck);
			model.addAttribute("followCheck", followCheck);
		}
		return "member/mypage";
	

	}

	@RequestMapping(value = "update", method = RequestMethod.GET)
	public void update(HttpSession session, Model model) throws Exception {
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
		memberDTO = memberService.detail(memberDTO);
		model.addAttribute("member", memberDTO);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(MemberDTO memberDTO,  HttpSession session, Model model) throws Exception {
		MemberDTO dtoTmp = (MemberDTO) session.getAttribute("member");

		memberDTO.setMemberId(dtoTmp.getMemberId());
	
		int num = memberService.update(memberDTO, session);
		memberService.login(memberDTO, session);
		return "redirect:/";
	}


    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String delete(Model model, HttpSession httpSession) throws Exception {
        MemberDTO dto = (MemberDTO) httpSession.getAttribute("member");

        // 회원 삭제 및 결과 메시지 처리
        String resultMessage = memberService.deleteMember(dto, httpSession);
        model.addAttribute("result", resultMessage);
        model.addAttribute("url", "/");

        return "/commons/message";
    }

	@GetMapping("follow")
	public String follow(FollowDTO followDTO, HttpSession session, Model model) throws Exception {
	      
		int result = memberService.follow(followDTO, session);
		model.addAttribute("msg", result);
		return "commons/result";
	
	}
	@GetMapping("fromFollowList")
	@ResponseBody
	public List<MemberDTO> fromFollowList(FollowDTO followDTO, Scroller scroller) throws Exception{
		List<MemberDTO>fromFollowList =memberService.fromFollowList(followDTO, scroller);
		return fromFollowList;
	}
	@GetMapping("toFollowList")
	@ResponseBody
	public List<MemberDTO> toFollowList(FollowDTO followDTO, Scroller scroller) throws Exception{
		List<MemberDTO>toFollowList =memberService.toFollowList(followDTO, scroller);
		return toFollowList;
	}
	@GetMapping("followCheck")
	@ResponseBody
	public Integer followCheck(FollowDTO followDTO, HttpSession session) throws Exception {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		if(memberDTO != null) {
			MemberDTO memberDTO2 = new MemberDTO();
			memberDTO2.setMemberNum(followDTO.getToFollow());
			memberDTO2 = memberService.detail(memberDTO2);
			if(memberDTO2.getMemberEmail() == null) {
				
				return  0;
			}
			followDTO.setFromFollow(memberDTO.getMemberNum());			
			return memberService.followCheck(followDTO);
		} else {
			return 0;
		}
		
	}
	
	@GetMapping("countFromFollow")
	@ResponseBody
	public Long countFromFollow (MemberDTO memberDTO) throws Exception {
		return memberService.countFromFollow(memberDTO);
	}
	
	@GetMapping("countToFollow")
	@ResponseBody
	public Long toFollow (MemberDTO memberDTO) throws Exception {
		return memberService.countToFollow(memberDTO);
	}
	
	@RequestMapping(value = "findID", method = RequestMethod.GET)
	public void findID(HttpSession session, Model model) throws Exception {
	}

	
	 // 아이디 찾기
	@RequestMapping(value = "findID", method = RequestMethod.POST)
    public String findID(MemberDTO memberDTO,Model model,HttpSession session) throws Exception {
		memberDTO = memberService.findID(memberDTO);
		model.addAttribute("member", memberDTO);
    
        try {
            
            if (memberDTO != null) {
                model.addAttribute("memberDTO", memberDTO);
                return "member/findIDResult"; // 아이디 찾기 결과 페이지로 이동
            } else {
                model.addAttribute("errorMessage", "해당 정보로 등록된 아이디가 없습니다.");
                return "member/findID"; // 다시 아이디 찾기 페이지로 이동
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "오류가 발생했습니다. 다시 시도해 주세요.");
            return "member/findID"; // 오류 발생 시 다시 아이디 찾기 페이지로 이동
        }
    }

	@RequestMapping(value = "findPassword", method = RequestMethod.GET)
	public void findPassword(HttpSession session, Model model) throws Exception {
	}

	
	 // 아이디 찾기
	@RequestMapping(value = "findPassword", method = RequestMethod.POST)
    public String findPassword(MemberDTO memberDTO,Model model,HttpSession session) throws Exception {
		memberDTO = memberService.findPassword(memberDTO);
		model.addAttribute("member", memberDTO);
    
        try {
            
            if (memberDTO != null) {
                model.addAttribute("memberDTO", memberDTO);
                return "/member/findPasswordResult"; // 아이디 찾기 결과 페이지로 이동
            } else {
                model.addAttribute("errorMessage", "해당 정보로 등록된 비밀번호가 없습니다.");
                return "/member/findPassword"; // 다시 아이디 찾기 페이지로 이동
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "오류가 발생했습니다. 다시 시도해 주세요.");
            return "/member/findPassword"; // 오류 발생 시 다시 아이디 찾기 페이지로 이동
        }
    }

	@RequestMapping(value = "proFileUpdate", method = RequestMethod.POST)
	public String proFileUpdate(MemberDTO memberDTO, MultipartFile files, HttpSession session, Model model) throws Exception {	

		int num = memberService.proFileUpdate(memberDTO, files,session);
		MemberDTO memberDTOSession = (MemberDTO)session.getAttribute("member");
		memberService.login(memberDTOSession, session);
		

		return "redirect:/";
	}

	 
	    
	}

	

