package com.api.aluno.services;

import com.api.aluno.dtos.AlunoDto;
import com.api.aluno.models.AlunoModel;
import com.api.aluno.repository.AlunoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public AlunoModel create(@Valid AlunoDto dto) {
        validarCampos(dto);
        AlunoModel aluno = new AlunoModel();
        aluno.setNome(dto.getNome());
        aluno.setCurso(dto.getCurso());
        aluno.setTelefone(dto.getTelefone());

        return alunoRepository.save(aluno);
    }

    public List<AlunoModel> listar() {
        return alunoRepository.findAll();
    }

    public AlunoModel buscar(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
    }

    public AlunoModel atualizar(@Valid AlunoDto dto, UUID id) {
        validarCampos(dto);
        AlunoModel alunoExistente = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));

        alunoExistente.setNome(dto.getNome());
        alunoExistente.setCurso(dto.getCurso());
        alunoExistente.setTelefone(dto.getTelefone());

        return alunoRepository.save(alunoExistente);
    }

    public void apagar(UUID id) {
        AlunoModel aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
        alunoRepository.delete(aluno);
    }

    public List<AlunoModel> buscarPorNome(String nomeBusca) {
        List<AlunoModel> alunos = alunoRepository.findByNomeContainingIgnoreCase(nomeBusca);
        if (alunos.isEmpty()) {
            throw new RuntimeException("Nenhum aluno encontrado com o nome: " + nomeBusca);
        }
        return alunos;
    }

    public List<AlunoModel> buscarPorCurso(String cursoBusca) {
        List<AlunoModel> alunos = alunoRepository.findByCursoContainingIgnoreCase(cursoBusca);
        if (alunos.isEmpty()) {
            throw new RuntimeException("Nenhum aluno encontrado no curso: " + cursoBusca);
        }
        return alunos;
    }

    private void validarCampos(AlunoDto dto) {
        if (dto.getNome() == null || dto.getNome().trim().length() <= 2) {
            throw new RuntimeException("Nome é obrigatório e deve ter mais de 2 caracteres.");
        }
        if (dto.getCurso() == null || dto.getCurso().trim().length() <= 2) {
            throw new RuntimeException("Curso é obrigatório e deve ter mais de 2 caracteres.");
        }
    }
}
