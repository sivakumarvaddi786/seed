// Define folders
folder('Folder1')
folder('Folder2')

// Define multi-pipeline jobs
multiPipeline('MultiJob1', 'Folder1')
multiPipeline('MultiJob2', 'Folder2')

// Define pipeline jobs
pipeline('PipelineJob1', 'Folder1')
pipeline('PipelineJob2', 'Folder2')

