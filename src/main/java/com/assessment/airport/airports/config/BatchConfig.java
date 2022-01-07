package com.assessment.airport.airports.config;

import javax.persistence.EntityManagerFactory;

import com.assessment.airport.airports.domaine.Country;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /*
     * @Autowired private CountryRepository countryRepository;
     */

    @Autowired
    EntityManagerFactory emf;

    @Value("classPath:/input/countries.csv")
    private Resource inputResource;

    @Bean
    public Job readCSVFileJob() {
        return jobBuilderFactory.get("readCSVFileJob").incrementer(new RunIdIncrementer()).listener(listener())
                .start(step()).build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step").<Country, Country>chunk(5).reader(reader()).processor(processor())
                .writer(writer()).build();
    }

    @Bean
    public ItemProcessor<Country, Country> processor() {
        return new DBLogProcessor();
    }

    @Bean
    public FlatFileItemReader<Country> reader() {
        FlatFileItemReader<Country> itemReader = new FlatFileItemReader<Country>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(inputResource);
        return itemReader;
    }

    @Bean
    public LineMapper<Country> lineMapper() {
        DefaultLineMapper<Country> lineMapper = new DefaultLineMapper<Country>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "id", "code", "name", "continent", "wikipedia_link", "keywords" });
        lineTokenizer.setIncludedFields(new int[] { 0, 1, 2, 3, 4, 5 });

        BeanWrapperFieldSetMapper<Country> fieldSetMapper = new BeanWrapperFieldSetMapper<Country>();
        fieldSetMapper.setTargetType(Country.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public JpaItemWriter<Country> writer() {

        JpaItemWriter<Country> writer = new JpaItemWriter<Country>();
        writer.setEntityManagerFactory(emf);
        return writer;
        /*
         *
         * itemWriter.setDataSource(dataSource()); itemWriter.setSql( "INSERT INTO
         * COUNTRY (id, code, name, continent, wikipedia );
         * itemWriter.setItemSqlParameterSourceProvider(new
         * BeanPropertyItemSqlParameterSourceProvider<Country>
         */
    }

    // @Bean
    /*
     * public DataSource dataSource() { EmbeddedDatabaseBuilder
     * embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder(); return
     * embeddedDatabaseBuilder.addScript(
     * "classpath:org/springframework/batch/core/schema-drop-h2.sql")
     * .addScript("classpath:org/springframework/batch/core/schema-h2.sql").setType(
     * EmbeddedDatabaseType.H2) .build(); }
     */

    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {
                /**
                 * As of now empty but can add some before job conditions
                 */
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    System.out.println(("!!! JOB FINISHED! Time to verify the results"));
                    /*
                     * countryRepository.findAll() .forEach(country -> System.out.println(("Found <"
                     * + country + "> in the database.")));
                     */
                }
            }
        };
    }

}
