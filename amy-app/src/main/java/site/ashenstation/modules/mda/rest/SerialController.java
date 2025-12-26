package site.ashenstation.modules.mda.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.modules.mda.dto.CreateSerialDto;
import site.ashenstation.modules.mda.service.SerialService;
import site.ashenstation.modules.mda.vo.MdaSerialVo;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/serial")
public class SerialController {
    private final SerialService serialService;

    @PostMapping("create")
    public ResponseEntity<Boolean> create(@Validated CreateSerialDto dto) {
        return ResponseEntity.ok(serialService.createSerial(dto));
    }

    @GetMapping("list")
    public ResponseEntity<List<MdaSerialVo>> list() {
        return ResponseEntity.ok(serialService.getSerialList());
    }
}
