package com.example.kakao.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kakao._core.errors.exception.Exception404;
import com.example.kakao.product.option.Option;
import com.example.kakao.product.option.OptionJPARepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductJPARepository productJPARepository;
    private final OptionJPARepository optionJPARepository;

    // (기능1) 상품 목록보기
    public List<ProductResponse.FindAllDTO> findAll(int page) {
        Pageable pageable = PageRequest.of(page,9);
        Page<Product> pageContent = productJPARepository.findAll(pageable);
        List<ProductResponse.FindAllDTO> responseDTOs = pageContent.getContent().stream()
                                                                            .map(product -> new ProductResponse.FindAllDTO(product))
                                                                            .collect(Collectors.toList());
        return responseDTOs;
    }

    // (기능2) 상품 상세보기
    public ProductResponse.FindByIdDTO findById(int id) {
        Product productPS = productJPARepository.findById(id).orElseThrow(() -> new Exception404("존재하는 상품이 아닙니다. : " + id));
        List<Option> optionListPSs = optionJPARepository.findByProductId(productPS.getId());

        return new ProductResponse.FindByIdDTO(productPS, optionListPSs);
    }

    // 상품조회 + 옵션조회
    public ProductResponse.FindByIdV1DTO findByIdV1(int id) {
        Product productPS = productJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 id의 상품을 찾을 수 없습니다 : " + id));

        List<Option> optionsPS = optionJPARepository.findByProductId(id);

        return new ProductResponse.FindByIdV1DTO(productPS, optionsPS);
    }

    // 양방향 조회
    public ProductResponse.FindByIdV2DTO findByIdV2(int id) {
        Product productPS = productJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 id의 상품을 찾을 수 없습니다 : " + id));
        System.out.println("아직 option을 Lazy Loading 하기 전입니다 =============");
        return new ProductResponse.FindByIdV2DTO(productPS);
    }

    // 옵션만 조회
    public ProductResponse.FindByIdV3DTO findByIdV3(int id) {
        List<Option> optionsPS = optionJPARepository.findByProductId(id);
        return new ProductResponse.FindByIdV3DTO(optionsPS);
    }

    // 엔티티 응답
    public List<Option> findByIdV4(int id) {
        List<Option> optionsPS = optionJPARepository.findByProductId(id);
        return optionsPS;
    }
}
