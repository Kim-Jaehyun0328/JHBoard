package JHboard.project.domain.board.controller;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.dto.BoardRsDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
public class BoardController {

  private final BoardService boardService;

  @GetMapping("/")
  public String home(Model model) {
    List<Board> boards = boardService.findAll();

    List<BoardRsDto> boardList = boards.stream()
        .map(BoardRsDto::toDto)
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
  public String createBoard(@Valid @ModelAttribute(value = "board") BoardRqDto boardRqDto, BindingResult bindingResult) {
    if(bindingResult.hasErrors()) {
      return "boards/createBoardForm";
    }
    boardService.create(Board.createEntity(boardRqDto));
    return "redirect:/";
  }

  @GetMapping("/board/{boardId}")
  public String detailBoard(@PathVariable(value = "boardId") Long boardId, Model model) {
    Optional<Board> board = boardService.findById(boardId);
    if(board.isEmpty()){
      throw new RuntimeException("존재하지 않은 게시글입니다.");
    }

    BoardRsDto boardRsDto = BoardRsDto.toDto(board.get());
    model.addAttribute("board", boardRsDto);

    board.get().updateView();  //조회 수 1 증가
    return "boards/detailBoardForm";
  }

  @GetMapping("/board/{boardId}/edit")
  public String editBoardPage(@PathVariable(value = "boardId") Long boardId, Model model) {

    Optional<Board> board = boardService.findById(boardId);

    BoardRsDto boardRsDto = BoardRsDto.toDto(board.get());

    model.addAttribute("board", boardRsDto);
    return "boards/editBoardForm";
  }

  @PostMapping("/board/{boardId}/edit")
  public String editBoard(@PathVariable(value = "boardId") Long boardId, @Valid @ModelAttribute(value = "board") BoardRqDto boardRqDto, BindingResult bindingResult) {
    if(bindingResult.hasErrors()){
      return "boards/editBoardForm";
    }

    boardService.updateBoard(boardId, boardRqDto);

    return "redirect:/board/" + String.valueOf(boardId);
  }

  @DeleteMapping("/board/{boardId}/delete")
  public String deleteBoard(@PathVariable(value = "boardId") Long boardId){
    boardService.delete(boardId);
    return "redirect:/";
  }

}
