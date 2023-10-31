import os
import openai

openai.api_key = os.getenv("OPENAI_API_KEY")

completion = openai.ChatCompletion.create(
  model="gpt-3.5-turbo",
  messages=[
    {"role": "system", "content": "You are a python software engineer."},

    #{"role": "user", "content": "Compose a python script to convert an input of temperature in celsius to fahrenheit"}
    #{"role": "user", "content": "Compose a python script to output the number of years, days, minutes, and seconds from a date in the past until now."}
    {"role": "user", "content": "Compose a python 3.0 script to output the number of years, days, minutes, and seconds from a date in the past until now."}
  ]
)

print(completion.choices[0].message)