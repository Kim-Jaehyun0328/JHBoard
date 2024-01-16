package JHboard.project.domain.board.controller;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.dto.BoardRsDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

  private final BoardService boardService;

  @GetMapping("/")
  public String home(Model model) {

    List<Board> boards = boardService.findAll();

    List<BoardRsDto> boardList = boards.stream()
        .map(BoardRsDto::toDtoForList)
        .collect(Collectors.toList());

    model.addAttribute("boardList", boardList);

    return "home";
  }

  @GetMapping("/board/new")
  public String createBoardPage(Model model) {
    model.addAttribute("board", new BoardRqDto());
    return "boards/createBoardForm";
  }

  @PostMapping("/board/new")
  public String createBoard(@Valid @ModelAttribute(value = "board") BoardRqDto boardRqDto, BindingResult bindingResult, Principal principal)
      throws IOException {
    if(bindingResult.hasErrors()) {
      return "boards/createBoardForm";
    }
    boardService.create(boardRqDto, principal);
    return "redirect:/";
  }

  @GetMapping("/board/{boardId}")
  public String detailBoard(@PathVariable(value = "boardId") Long boardId, Model model) {
    BoardRsDto boardRsDto = boardService.detailBoard(boardId);
    model.addAttribute("board", boardRsDto);

    return "boards/detailBoardForm";
  }

  @GetMapping("/board/{boardId}/edit")
  public String editBoardPage(@PathVariable(value = "boardId") Long boardId, Model model, Principal principal) {

    if(boardService.checkUser(boardId, principal)){ //맞다면
      Optional<Board> board = boardService.findById(boardId);
      BoardRsDto boardRsDto = BoardRsDto.toDto(board.get());
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
