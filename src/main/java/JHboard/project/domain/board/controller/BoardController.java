package JHboard.project.domain.board.controller;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.dto.BoardRsDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import JHboard.project.domain.comment.dto.CommentRqDto;
import JHboard.project.domain.like.service.LikeService;
import JHboard.project.domain.member.dto.CustomUserDetails;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

  private final BoardService boardService;
  private final LikeService likeService;

  @GetMapping("/test")
  public String test(){
    return "index";
  }

  @GetMapping("/")
  public String home(Model model, @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable
  , @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "sort", required = false) String sort) {
    Page<BoardRsDto> boards;

    if(StringUtils.hasText(keyword)){
      boards = boardService.searchBoards(keyword, pageable).map(BoardRsDto::toDtoForList);
    } else{
      boards = boardService.findAll(pageable).map(BoardRsDto::toDtoForList);
    }

    //요청한 페이지가 0보다 작거나 줄 수 있는 페이지보다 클 때
    if(boards.getTotalPages() <= pageable.getPageNumber()){
      log.info("hello");
      return "error/403";
    }


    model.addAttribute("boardList", boards);
    model.addAttribute("keyword", keyword);
    model.addAttribute("sort", sort);

    log.info("================================================");
    log.info("totalPage={}", boards.getTotalPages());
    log.info("number={}", boards.getNumber());
    log.info("numberOfElements={}", boards.getNumberOfElements());
    log.info("size={}", boards.getSize());
    log.info("totalElements={}", boards.getTotalElements());
    log.info("================================================");
    return "home";
  }

  @GetMapping("/board/new")
  public String createBoardPage(Model model) {
    model.addAttribute("board", new BoardRqDto());
    return "boards/createBoardForm";
  }

  @PostMapping("/board/new")
  public String createBoard(@Valid @ModelAttribute(value = "board") BoardRqDto boardRqDto, BindingResult bindingResult, Principal principal, Model model)
      throws IOException {
    if(bindingResult.hasErrors()) {
      return "boards/createBoardForm";
    }
    if(boardRqDto.getContent().equals("")){ //빈 텍스트라면
      return "boards/createBoardForm";
    }
    boardService.create(boardRqDto, principal);
    return "redirect:/";
  }

  @GetMapping("/board/{boardId}")
  public String detailBoard(@PathVariable(value = "boardId") Long boardId, Model model
      , @AuthenticationPrincipal CustomUserDetails userDetails) {
    boolean like = false;

    if(userDetails != null){
      Long memberId = userDetails.getMember().getId();
      like = likeService.findLike(boardId, memberId);
    }


    BoardRsDto boardRsDto = boardService.detailBoard(boardId);
    model.addAttribute("board", boardRsDto);
    model.addAttribute("like", like);
    model.addAttribute("commentForm", new CommentRqDto());

    return "boards/detailBoardForm";
  }


  @GetMapping("/board/{boardId}/edit")
  public String editBoardPage(@PathVariable(value = "boardId") Long boardId, Model model, Principal principal) {

    if(boardService.checkUser(boardId, principal)){ //맞다면
      Optional<Board> board = boardService.findById(boardId);
      BoardRsDto boardRsDto = BoardRsDto.toDtoEdit(board.get());
      model.addAttribute("board", boardRsDto);
      return "boards/editBoardForm";
    }
    return "error/403";
  }

  @PostMapping("/board/{boardId}/edit")
  public String editBoard(@PathVariable(value = "boardId") Long boardId, @Valid @ModelAttribute(value = "board") BoardRqDto boardRqDto
      , Principal principal, BindingResult bindingResult) throws IOException {
    if(bindingResult.hasErrors()){
      return "boards/editBoardForm";
    }
    if(boardService.checkUser(boardId, principal)){
      boardService.updateBoard(boardId, boardRqDto);
      return "redirect:/board/" + boardId;
    }
    return "error/403";
  }

  @DeleteMapping("/board/{boardId}/delete")
  public String deleteBoard(@PathVariable(value = "boardId") Long boardId, Principal principal){
    if(boardService.checkUser(boardId, principal)){
      boardService.delete(boardId);
      return "redirect:/";
    }
   return "error/403";
  }
}
