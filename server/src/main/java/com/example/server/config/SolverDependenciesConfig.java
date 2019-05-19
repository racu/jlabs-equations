package com.example.server.config;

import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.storage.LocalTestCasesStorage;
import com.example.jlabscomp.verifier.SolutionVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolverDependenciesConfig {

    @Bean
    public LocalTestCasesStorage getStorage(){
        return new LocalTestCasesStorage();
    }

    @Bean
    public SolutionVerifier getVerifier(){
        return new SolutionVerifier();
    }

    @Bean
    public FastEquationParser getParser(){
        return new FastEquationParser();
    }
}
