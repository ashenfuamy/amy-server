package site.ashenstation.modules.mda.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.modules.mda.dto.CreateSerialDto;
import site.ashenstation.modules.mda.service.SerialService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/serial")
public class SerialController {
    private final SerialService serialService;

    @PostMapping("create")
    public void create( @Validated CreateSerialDto dto) {
        serialService.createSerial(dto);
    }
}
